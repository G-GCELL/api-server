package com.gabia.weat.gcellapiserver.dto.log;

import static com.gabia.weat.gcellapiserver.dto.log.ApiLogFormatDto.*;
import static com.gabia.weat.gcellapiserver.dto.log.ErrorLogFormatDto.*;
import static com.gabia.weat.gcellapiserver.dto.log.MessageBrokerLogFormatDto.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LogFormatFactory {

	@Value("${server.name}")
	private String serverName;
	private final int TRACE_ID_LENGTH = 7;
	private ThreadLocal<String> traceInfoHolder = new ThreadLocal<>();

	public void startTrace() {
		if (traceInfoHolder.get() == null) {
			traceInfoHolder.set(RandomStringUtils.random(TRACE_ID_LENGTH, true, true));
		}
	}

	public void startTrace(String traceId) {
		traceInfoHolder.set(traceId);
	}

	public void endTrace() {
		traceInfoHolder.remove();
	}

	public ApiLogFormatDtoBuilder getApiLogFormatBuilder() {
		return ApiLogFormatDto.builder()
			.level(Level.INFO)
			.serverName(serverName)
			.traceId(this.traceInfoHolder.get());
	}

	public ErrorLogFormatDtoBuilder getErrorLogFormatBuilder() {
		return ErrorLogFormatDto.builder()
			.level(Level.ERROR)
			.serverName(serverName)
			.traceId(this.traceInfoHolder.get());
	}

	public MessageBrokerLogFormatDtoBuilder getMessageBrokerLogFormatBuilder() {
		return MessageBrokerLogFormatDto.builder()
			.level(Level.INFO)
			.serverName(serverName)
			.traceId(this.traceInfoHolder.get());
	}

	public String getTraceId() {
		return this.traceInfoHolder.get();
	}

}