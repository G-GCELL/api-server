package com.gabia.weat.gcellapiserver.service.consumer;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.domain.type.MessageType;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateProgressMsgDto;
import com.gabia.weat.gcellapiserver.service.MessageService;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileCreateProgressConsumer implements Consumer<FileCreateProgressMsgDto> {

	private final MessageService messageService;

	@Override
	@RabbitListener(queues = "#{fileCreateProgressQueue}", containerFactory = "fileCreateProgressListenerFactory")
	public void receiveMessage(FileCreateProgressMsgDto message, Channel channel,
		@Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
		sendFileCreateProgressMessage(message);
		channel.basicAck(tag, false);
	}

	private void sendFileCreateProgressMessage(FileCreateProgressMsgDto fileCreateProgressMsgDto) {
		messageService.sendMessageToMemberId(
			fileCreateProgressMsgDto.memberId(),
			MessageType.FILE_CREATION_PROGRESS,
			fileCreateProgressMsgDto
		);
	}

}