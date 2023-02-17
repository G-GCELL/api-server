package com.gabia.weat.gcellapiserver.service.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.annotation.MessageLog;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateRequestMsgDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCreateRequestProducer implements Producer<FileCreateRequestMsgDto> {

	private final RabbitTemplate fileCreateRequestRabbitTemplate;

	@Override
	@MessageLog(exchange = "@directExchange.getName()", queue = "@fileCreateRequestQueue.getName()")
	public void sendMessage(FileCreateRequestMsgDto message) {
		CorrelationData correlationData = new CorrelationData();
		fileCreateRequestRabbitTemplate.correlationConvertAndSend(message, correlationData);
	}

}