package com.gabia.weat.gcellapiserver.service.consumer;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.annotation.ConsumerLog;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateProgressMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellapiserver.service.MessageHandler;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileCreateProgressConsumer implements Consumer<FileCreateProgressMsgDto> {

	private final MessageHandler messageHandler;

	@Override
	@ConsumerLog(queue = "${rabbitmq.queue.file-create-progress-queue}")
	@RabbitListener(containerFactory = "fileCreateProgressListenerFactory")
	public void receiveMessage(MessageWrapperDto<FileCreateProgressMsgDto> message, Channel channel,
		@Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
		messageHandler.sendCreateExcelMsg(message);
		channel.basicAck(tag, false);
	}

}