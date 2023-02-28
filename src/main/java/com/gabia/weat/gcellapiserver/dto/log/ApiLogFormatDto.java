package com.gabia.weat.gcellapiserver.dto.log;

import org.slf4j.event.Level;
import org.springframework.http.HttpMethod;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiLogFormatDto extends LogFormatDto {

	private String userIp;
	private HttpMethod httpMethod;
	private String apiUri;
	private boolean success;
	private int status;
	private int detailStatus;
	private long time;
	private String input;

	@Builder
	public ApiLogFormatDto(Level level, String serverName, String traceId, String userIp, HttpMethod httpMethod,
		String apiUri, boolean success, int status, int detailStatus, long time, String input) {
		super(level, serverName, traceId);
		this.userIp = userIp;
		this.httpMethod = httpMethod;
		this.apiUri = apiUri;
		this.success = success;
		this.status = status;
		this.detailStatus = detailStatus;
		this.time = time;
		this.input = input;
	}

	@Override
	public String toString() {
		return String.format("[%s] %s [%s] %s %s, %s[%d-%d], times-%dms, input: %s",
			this.serverName,
			this.userIp,
			this.traceId,
			this.httpMethod.name(),
			this.apiUri,
			this.success? "success" : "fail",
			this.status,
			this.detailStatus,
			this.time,
			this.input
		);
	}

}