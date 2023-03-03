package com.gabia.weat.gcellapiserver.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.event.Level;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabia.weat.gcellapiserver.annotation.ConsumerLog;
import com.gabia.weat.gcellapiserver.annotation.ProducerLog;
import com.gabia.weat.gcellapiserver.domain.type.TargetType;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellapiserver.dto.log.ApiLogFormatDto.ApiLogFormatDtoBuilder;
import com.gabia.weat.gcellapiserver.dto.log.LogFormatFactory;
import com.gabia.weat.gcellapiserver.dto.log.MessageBrokerLogFormatDto.MessageBrokerLogFormatDtoBuilder;
import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.service.log.LogPrinter;
import com.gabia.weat.gcellapiserver.parser.CustomExpressionParser;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

	private final String X_FORWARDED_FOR_HEADER = "X-FORWARDED-FOR";
	private final LogFormatFactory logFormatFactory;
	private final LogPrinter logPrinter;
	private final CustomExpressionParser expressionBeanParser;

	@Around("within(com.gabia.weat.gcellapiserver.controller..*)")
	public Object apiLogAdvisor(ProceedingJoinPoint joinPoint) throws Throwable {
		logFormatFactory.startTrace();
		ApiLogFormatDtoBuilder logFormatBuilder = this.getDefaultApiLogFormatBuilder();
		long startTime = System.currentTimeMillis();
		try {
			Object result = joinPoint.proceed();
			this.successStatus(logFormatBuilder, result);
			return result;
		} catch (CustomException e) {
			this.failStatus(logFormatBuilder, e.getErrorCode());
			throw e;
		} catch (Exception e) {
			ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;
			this.failStatus(logFormatBuilder, errorCode);
			this.printErrorLog(e);
			throw new CustomException(errorCode);
		} finally {
			long time = System.currentTimeMillis() - startTime;
			String input = this.getInput(joinPoint);
			this.printApiLog(logFormatBuilder, time, input);
			logFormatFactory.endTrace();
		}
	}

	@Around("@annotation(consumerLog)")
	public Object consumerLogAdvisor(ProceedingJoinPoint joinPoint, ConsumerLog consumerLog) throws Throwable {
		this.setTraceId(joinPoint);
		try {
			this.printMessageBrokerLog(TargetType.CONSUMER, consumerLog.queue(), this.getInput(joinPoint), null);
			return joinPoint.proceed();
		} catch (Exception e) {
			this.printErrorLog(e);
		}
		return null;
	}

	@Around("@annotation(producerLog)")
	public Object producerLogAdvisor(ProceedingJoinPoint joinPoint, ProducerLog producerLog) throws Throwable {
		Exception exception = null;
		try {
			return joinPoint.proceed();
		} catch (Exception e) {
			exception = e;
			throw e;
		} finally {
			this.printMessageBrokerLog(TargetType.PRODUCER, producerLog.exchange(), this.getInput(joinPoint),
				exception);
		}
	}

	private void setTraceId(ProceedingJoinPoint joinPoint) {
		Object[] args = joinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof MessageWrapperDto<?> dto) {
				logFormatFactory.startTrace(dto.getTraceId());
				break;
			}
		}
	}

	private HttpServletRequest getRequestInfo() {
		return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	private ApiLogFormatDtoBuilder getDefaultApiLogFormatBuilder() {
		HttpServletRequest request = this.getRequestInfo();
		return logFormatFactory.getApiLogFormatBuilder()
			.httpMethod(HttpMethod.valueOf(request.getMethod()))
			.userIp(this.getRemoteAddr(request))
			.apiUri(request.getRequestURI());
	}

	protected String getRemoteAddr(HttpServletRequest request) {
		String header = request.getHeader(X_FORWARDED_FOR_HEADER);
		return (header != null) ? header : request.getRemoteAddr();
	}

	private void successStatus(ApiLogFormatDtoBuilder logFormatBuilder, Object result) {
		int status = result instanceof ResponseEntity response ?
			response.getStatusCode().value() : HttpStatus.OK.value();
		logFormatBuilder.success(true).status(status);
	}

	private void failStatus(ApiLogFormatDtoBuilder logFormatBuilder, ErrorCode errorCode) {
		logFormatBuilder.success(false)
			.status(errorCode.getStatus().value())
			.detailStatus(errorCode.getCode().getStatus());
	}

	private String getInput(ProceedingJoinPoint joinPoint) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		CodeSignature methodSignature = (CodeSignature)joinPoint.getSignature();
		String[] paramNames = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();

		StringBuilder input = new StringBuilder("{");
		for (int index = 0; index < paramNames.length; index++) {
			input.append(paramNames[index]);
			input.append(" : ");
			input.append(this.parseJson(objectMapper, args[index]));
			if (index != paramNames.length - 1)
				input.append(", ");
		}
		input.append("}");
		return input.toString();
	}

	private String parseJson(ObjectMapper mapper, Object value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			return value.toString();
		}
	}

	private void printApiLog(ApiLogFormatDtoBuilder logFormatBuilder, long time, String input) {
		logFormatBuilder
			.time(time)
			.input(input)
			.build();
		logPrinter.print(logFormatBuilder.build());
	}

	private void printErrorLog(Exception e) {
		StackTraceElement stackTraceElement = e.getStackTrace()[0];
		logPrinter.print(logFormatFactory.getErrorLogFormatBuilder()
			.className(stackTraceElement.getClassName())
			.methodName(stackTraceElement.getMethodName())
			.exceptionName(e.getClass().getName())
			.message(e.getMessage())
			.build());
	}

	private void printMessageBrokerLog(TargetType type, String target, String input, Exception exception) {
		String targetName = (String)expressionBeanParser.parse(target);
		MessageBrokerLogFormatDtoBuilder logFormatDtoBuilder = logFormatFactory.getMessageBrokerLogFormatBuilder()
			.level(exception == null? Level.INFO : Level.ERROR)
			.type(type)
			.exchangeName(type == TargetType.PRODUCER ? targetName : null)
			.queueName(type == TargetType.CONSUMER ? targetName : null)
			.exception(exception)
			.input(input);

		logPrinter.print(logFormatDtoBuilder.build());
	}

}