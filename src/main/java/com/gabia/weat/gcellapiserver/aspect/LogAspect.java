package com.gabia.weat.gcellapiserver.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabia.weat.gcellapiserver.annotation.ConsumerLog;
import com.gabia.weat.gcellapiserver.annotation.ProducerLog;
import com.gabia.weat.gcellapiserver.domain.type.TargetType;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellapiserver.dto.log.LogFormatFactory;
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
		long startTime = System.currentTimeMillis();
		boolean success = true;
		int status = 200;
		int detailStatus = 0;
		try {
			Object result = joinPoint.proceed();
			return result;
		} catch (CustomException e) {
			success = false;
			status = e.getErrorCode().getStatus().value();
			detailStatus = e.getErrorCode().getCustomStatus().getCode();
			throw e;
		} catch (Exception e) {
			success = false;
			ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;
			status = errorCode.getStatus().value();
			detailStatus = errorCode.getCustomStatus().getCode();
			logPrinter.printErrorLog(e);
			throw new CustomException(errorCode);
		} finally {
			long time = System.currentTimeMillis() - startTime;
			String input = this.getInput(joinPoint);
			logPrinter.printApiLog(this.getRequestInfo(), success, status, detailStatus, time, input);
			logFormatFactory.endTrace();
		}
	}

	@Around("@annotation(consumerLog)")
	public Object consumerLogAdvisor(ProceedingJoinPoint joinPoint, ConsumerLog consumerLog) throws Throwable {
		this.setTraceId(joinPoint);
		logPrinter.printMessageBrokerLog(TargetType.CONSUMER, consumerLog.queue(), this.getInput(joinPoint), null);
		return joinPoint.proceed();
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
			logPrinter.printMessageBrokerLog(
				TargetType.PRODUCER,
				producerLog.exchange(),
				this.getInput(joinPoint),
				exception
			);
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

	private HttpServletRequest getRequestInfo() {
		return ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	}

}
