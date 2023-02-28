package com.gabia.weat.gcellapiserver.service.consumer;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.annotation.ConsumerLog;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateErrorMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;
import com.rabbitmq.client.Channel;

@Service
public class FileCreateErrorConsumer implements Consumer<FileCreateErrorMsgDto> {

	@Override
	@ConsumerLog(queue = "${rabbitmq.queue.file-create-error-queue}")
	@RabbitListener(containerFactory = "fileCreateErrorListenerFactory")
	public void receiveMessage(MessageWrapperDto<FileCreateErrorMsgDto> message, Channel channel,
		@Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
		// 에러 처리
		channel.basicAck(tag, false);
	}

}
