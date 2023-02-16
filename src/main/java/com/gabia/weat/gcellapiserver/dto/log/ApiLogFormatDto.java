package com.gabia.weat.gcellapiserver.dto.log;

import java.time.LocalDateTime;

import org.slf4j.event.Level;
import org.springframework.http.HttpMethod;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiLogFormatDto extends LogFormatDto {

	private HttpMethod httpMethod;
	private String apiUri;
	private boolean success;
	private int status;
	private long time;
	private String input;

	@Builder
	public ApiLogFormatDto(Level level, String serverName, String traceId, HttpMethod httpMethod,
		String apiUri, boolean success, int status, long time, String input) {
		super(level, serverName, traceId);
		this.httpMethod = httpMethod;
		this.apiUri = apiUri;
		this.success = success;
		this.status = status;
		this.time = time;
		this.input = input;
	}

	@Override
	public String toString() {
		return String.format("[%s] %s [%s] %s %s, %s[%d], times-%dms, input: %s",
			this.serverName,
			LocalDateTime.now(),
			this.traceId,
			this.httpMethod.name(),
			this.apiUri,
			this.success? "success" : "fail",
			this.status,
			this.time,
			this.input
		);
	}

}