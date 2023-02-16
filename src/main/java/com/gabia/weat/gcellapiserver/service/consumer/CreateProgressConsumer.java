package com.gabia.weat.gcellapiserver.service.consumer;

import java.io.IOException;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.domain.type.MessageType;
import com.gabia.weat.gcellapiserver.dto.MessageDto.CreateProgressMsgDto;
import com.gabia.weat.gcellapiserver.service.MessageService;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateProgressConsumer {

	private final MessageService messageService;
	private final Queue creationProgressQueue;

	@RabbitListener(queues = "#{creationProgressQueue.getName()}")
	public void receiveMessage(CreateProgressMsgDto createProgressMsgDto, Channel channel,
		@Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
		sendCreateProgressMessage(createProgressMsgDto);
		channel.basicAck(tag, false);
	}

	private void sendCreateProgressMessage(CreateProgressMsgDto createProgressMsgDto) {
		messageService.sendMessageToMemberId(
			createProgressMsgDto.memberId(),
			MessageType.FILE_CREATION_PROGRESS,
			createProgressMsgDto
		);
	}

}