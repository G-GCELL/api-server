package com.gabia.weat.gcellapiserver.service.producer;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellcommonmodule.annotation.ProducerLog;
import com.gabia.weat.gcellcommonmodule.dto.MessageDto.CsvUpdateRequestMsgDto;
import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;
import com.gabia.weat.gcellcommonmodule.error.ErrorCode;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvUpdateRequestProducer implements Producer<CsvUpdateRequestMsgDto>{

	private final RabbitTemplate csvUpdateRequestRabbitTemplate;

	@Override
	@ProducerLog(exchange = "${rabbitmq.exchange.direct-exchange}")
	public void sendMessage(MessageWrapperDto<CsvUpdateRequestMsgDto> message) {
		try {
			csvUpdateRequestRabbitTemplate.convertAndSend(message);
		} catch (AmqpException e) {
			throw new CustomException(ErrorCode.MESSAGE_BROKER_CONNECTION_ERROR);
		}
	}

}