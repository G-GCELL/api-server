package com.gabia.weat.gcellapiserver.service.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateRequestMsgDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateRequestProducer {

	private final RabbitTemplate rabbitTemplate;

	public void sendMessage(FileCreateRequestMsgDto fileCreateRequestMsgDto) {
		CorrelationData correlationData = new CorrelationData();
		rabbitTemplate.correlationConvertAndSend(fileCreateRequestMsgDto, correlationData);
	}

}
