package com.gabia.weat.gcellapiserver.service.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellcommonmodule.annotation.ProducerLog;
import com.gabia.weat.gcellcommonmodule.dto.MessageDto.FileCreateRequestMsgDto;
import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;
import com.gabia.weat.gcellcommonmodule.error.ErrorCode;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCreateRequestProducer implements Producer<FileCreateRequestMsgDto> {

	private final RabbitTemplate fileCreateRequestRabbitTemplate;

	@Override
	@ProducerLog(exchange = "${rabbitmq.exchange.direct-exchange}")
	public void sendMessage(MessageWrapperDto<FileCreateRequestMsgDto> message) {
		try {
			fileCreateRequestRabbitTemplate.convertAndSend(message);
		} catch (AmqpException e) {
			throw new CustomException(ErrorCode.MESSAGE_BROKER_CONNECTION_ERROR);
		}
	}

}