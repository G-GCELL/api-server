package com.gabia.weat.gcellapiserver.service.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.annotation.ProducerLog;
import com.gabia.weat.gcellapiserver.dto.MessageDto.CsvUpdateRequestMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvUpdateRequestProducer implements Producer<CsvUpdateRequestMsgDto>{

	private final RabbitTemplate csvUpdateRequestRabbitTemplate;

	@Override
	@ProducerLog(exchange = "${rabbitmq.exchange.direct-exchange}")
	public void sendMessage(MessageWrapperDto<CsvUpdateRequestMsgDto> message) {
		CorrelationData correlationData = new CorrelationData();
		csvUpdateRequestRabbitTemplate.correlationConvertAndSend(message, correlationData);
	}

}