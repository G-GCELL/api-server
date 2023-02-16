package com.gabia.weat.gcellapiserver.dto.log;

import java.time.LocalDateTime;

import org.slf4j.event.Level;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageBrokerLogFormatDto extends LogFormatDto {

	private static final String SEND_SUCCESS_PREFIX = "----->";
	private static final String SEND_FAIL_PREFIX = "--X-->";

	private String exchangeName;
	private String queueName;
	private boolean success;
	private String exceptionName;
	private String input;

	@Builder
	public MessageBrokerLogFormatDto(Level level, String serverName, String traceId, String exchangeName,
		String queueName, boolean success, String exceptionName, String input) {
		super(level, serverName, traceId);
		this.exchangeName = exchangeName;
		this.queueName = queueName;
		this.success = success;
		this.exceptionName = exceptionName;
		this.input = input;
	}

	@Override
	public String toString() {
		StringBuilder log = new StringBuilder(String.format("[%s] %s [%s] (%s)",
			this.serverName,
			LocalDateTime.now(),
			this.traceId,
			this.exchangeName
		));
		log.append(success ? SEND_SUCCESS_PREFIX : SEND_FAIL_PREFIX);
		log.append("(" + queueName + ")");
		if (success) {
			log.append("cause: " + exceptionName + ", ");
		}
		log.append("input: " + input);
		return log.toString();
	}

}