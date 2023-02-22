package com.gabia.weat.gcellapiserver.dto.log;

import java.time.LocalDateTime;

import org.slf4j.event.Level;

import com.gabia.weat.gcellapiserver.domain.type.TargetType;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageBrokerLogFormatDto extends LogFormatDto {

	private static final String SEND_SUCCESS_PREFIX = "-----> ";

	private TargetType type;
	private String exchangeName;
	private String queueName;
	private String input;

	@Builder
	public MessageBrokerLogFormatDto(Level level, String serverName, String traceId, TargetType type,
		String exchangeName,
		String queueName, String input) {
		super(level, serverName, traceId);
		this.type = type;
		this.exchangeName = exchangeName;
		this.queueName = queueName;
		this.input = input;
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
		log.append(SEND_SUCCESS_PREFIX);
		log.append(String.format("(%s) ", exchangeName));
		log.append(String.format("input: %s", input));
		return log.toString();
	}

	private StringBuilder frontFormat() {
		return new StringBuilder(String.format("[%s] %s [%s] ",
			this.serverName,
			LocalDateTime.now(),
			this.traceId
		));
	}

}
