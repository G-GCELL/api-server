package com.gabia.weat.gcellapiserver.dto.log;

import org.slf4j.event.Level;

import com.gabia.weat.gcellapiserver.domain.type.TargetType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageBrokerLogFormatDto extends LogFormatDto {

	private static final String SEND_SUCCESS_PREFIX = "-----> ";
	private static final String SEND_FAIL_PREFIX = "--X--> ";

	private TargetType type;
	private String exchangeName;
	private String queueName;
	private String input;
	private Exception exception;

	@Builder
	public MessageBrokerLogFormatDto(Level level, String serverName, String traceId, TargetType type,
		String exchangeName,
		String queueName, String input, Exception exception) {
		super(level, serverName, traceId);
		this.type = type;
		this.exchangeName = exchangeName;
		this.queueName = queueName;
		this.input = input;
		this.exception = exception;
	}

	@Override
	public String toString() {
		return switch (type) {
			case CONSUMER -> this.toConsumerFormat();
			case PRODUCER -> this.toProducerFormat();
		};
	}

	private String toConsumerFormat() {
		StringBuilder log = frontFormat();
		log.append(String.format("(%s) ", queueName));
		log.append(SEND_SUCCESS_PREFIX);
		log.append(String.format("input: %s", input));
		return log.toString();
	}

	private String toProducerFormat() {
		StringBuilder log = frontFormat();
		log.append(exception == null? SEND_SUCCESS_PREFIX : SEND_FAIL_PREFIX);
		log.append(String.format("(%s) ", exchangeName));
		if (exception != null) {
			StackTraceElement stackTraceElement = exception.getStackTrace()[0];
			log.append(String.format("cause: %s-%s ", stackTraceElement.getClassName(), stackTraceElement.getMethodName()));
			log.append(String.format("message: %s ", exception.getMessage()));
		}
		log.append(String.format("input: %s", input));
		return log.toString();
	}

	private StringBuilder frontFormat() {
		return new StringBuilder(String.format("[%s] [%s] ",
			this.serverName,
			this.traceId
		));
	}

}
