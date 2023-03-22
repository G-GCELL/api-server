package com.gabia.weat.gcellapiserver.service.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.service.MessageHandler;
import com.gabia.weat.gcellcommonmodule.annotation.ConsumerLog;
import com.gabia.weat.gcellcommonmodule.dto.MessageDto.FileCreateErrorMsgDto;
import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCreateErrorConsumer implements Consumer<FileCreateErrorMsgDto> {

	private final MessageHandler messageHandler;

	@Override
	@ConsumerLog(queue = "${rabbitmq.queue.file-create-error-queue}")
	@RabbitListener(containerFactory = "fileCreateErrorListenerFactory", errorHandler = "customListenerErrorHandler")
	public void receiveMessage(MessageWrapperDto<FileCreateErrorMsgDto> message) throws CustomException {
		messageHandler.sendErrorMsg(message);
	}

}