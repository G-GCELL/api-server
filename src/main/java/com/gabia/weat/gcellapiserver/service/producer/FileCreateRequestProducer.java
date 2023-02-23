package com.gabia.weat.gcellapiserver.service.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.annotation.ProducerLog;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateRequestMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCreateRequestProducer implements Producer<FileCreateRequestMsgDto> {

	private final RabbitTemplate fileCreateRequestRabbitTemplate;

	@Override
	@ProducerLog(exchange = "${rabbitmq.exchange.direct-exchange}")
	public void sendMessage(MessageWrapperDto<FileCreateRequestMsgDto> message) {
		CorrelationData correlationData = new CorrelationData();
		fileCreateRequestRabbitTemplate.correlationConvertAndSend(message, correlationData);
	}

}