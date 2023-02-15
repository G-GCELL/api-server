package com.gabia.weat.gcellapiserver.service.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateRequestMsgDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateRequestProducer implements Producer<FileCreateRequestMsgDto> {

	private final RabbitTemplate creationRequestRabbitTemplate;

	@Override
	public void sendMessage(FileCreateRequestMsgDto message) {
		CorrelationData correlationData = new CorrelationData();
		creationRequestRabbitTemplate.correlationConvertAndSend(message, correlationData);
	}

}