package com.gabia.weat.gcellapiserver.service.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.service.MessageHandler;
import com.gabia.weat.gcellcommonmodule.annotation.ConsumerLog;
import com.gabia.weat.gcellcommonmodule.dto.MessageDto.FileCreateProgressMsgDto;
import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileCreateProgressConsumer implements Consumer<FileCreateProgressMsgDto> {

	private final MessageHandler messageHandler;

	@Override
	@ConsumerLog(queue = "${rabbitmq.queue.file-create-progress-queue}")
	@RabbitListener(containerFactory = "fileCreateProgressListenerFactory", errorHandler = "customListenerErrorHandler")
	public void receiveMessage(MessageWrapperDto<FileCreateProgressMsgDto> message) throws CustomException {
		messageHandler.sendCreateExcelMsg(message);
	}

}