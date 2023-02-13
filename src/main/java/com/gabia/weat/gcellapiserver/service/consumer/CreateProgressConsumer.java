package com.gabia.weat.gcellapiserver.service.consumer;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.dto.MessageDto.CreateProgressMsgDto;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreateProgressConsumer {

	@RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
	public void receiveMessage(CreateProgressMsgDto createProgressMsgDto, Channel channel,
		@Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {

		// 임시 코드
		log.info(String.valueOf(createProgressMsgDto));

		// SSE 메시지 전송

		channel.basicAck(tag, false);
	}

}