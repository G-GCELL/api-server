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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabia.weat.gcellapiserver.annotation.MessageLog;
import com.gabia.weat.gcellapiserver.dto.log.ApiLogFormatDto.ApiLogFormatDtoBuilder;
import com.gabia.weat.gcellapiserver.dto.log.LogFormatFactory;
import com.gabia.weat.gcellapiserver.dto.log.MessageBrokerLogFormatDto.MessageBrokerLogFormatDtoBuilder;
import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.service.log.LogPrinter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

	private final LogFormatFactory logFormatFactory;
	private final LogPrinter logPrinter;

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

	@Around("@annotation(messageLog)")
	public void messageBrokerLogAdvisor(ProceedingJoinPoint joinPoint, MessageLog messageLog) throws Throwable {
		boolean success = true;
		Exception throwException = null;
		try {
			joinPoint.proceed();
		} catch (Exception e) {
			success = false;
			throwException = e;
			throw e;
		} finally {
			String input = this.getInput(joinPoint);
			this.printMessageBrokerLog(success, messageLog, throwException, input);
		}
	}

	private HttpServletRequest getRequestInfo() {
		return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	}

	private ApiLogFormatDtoBuilder getDefaultApiLogFormatBuilder() {
		HttpServletRequest request = this.getRequestInfo();
		return logFormatFactory.getApiLogFormatBuilder()
			.httpMethod(HttpMethod.valueOf(request.getMethod()))
			.apiUri(request.getRequestURI());
	}

	private void successStatus(ApiLogFormatDtoBuilder logFormatBuilder, Object result) {
		int status = (result instanceof ResponseEntity response) ?
			response.getStatusCode().value() : HttpStatus.OK.value();
		logFormatBuilder.success(true).status(status);
	}

	private void failStatus(ApiLogFormatDtoBuilder logFormatBuilder, ErrorCode errorCode) {
		logFormatBuilder.success(false).status(errorCode.getCode().getStatus());
	}

	private String getInput(ProceedingJoinPoint joinPoint) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		CodeSignature methodSignature = (CodeSignature)joinPoint.getSignature();
		String[] paramNames = methodSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();

		StringBuilder input = new StringBuilder("{");
		for (int index = 0; index < paramNames.length; index++) {
			input.append(paramNames[index] + " : " + objectMapper.writeValueAsString(args[index]));
			if (index != paramNames.length - 1)
				input.append(", ");
		}
		input.append("}");
		return input.toString();
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

	private void printMessageBrokerLog(boolean success, MessageLog messageLog, Exception exception, String input) {
		MessageBrokerLogFormatDtoBuilder logFormatDtoBuilder = logFormatFactory.getMessageBrokerLogFormatBuilder()
			.level(success ? Level.INFO : Level.ERROR)
			.exchangeName(messageLog.exchange())
			.queueName(messageLog.queue())
			.success(success)
			.input(input);

		if (exception != null) {
			logFormatDtoBuilder.exceptionName(exception.getClass().getName())
				.message(exception.getMessage());
		}

		logPrinter.print(logFormatDtoBuilder.build());

	}

}