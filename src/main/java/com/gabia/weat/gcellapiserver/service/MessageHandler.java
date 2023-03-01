package com.gabia.weat.gcellapiserver.service;

import static com.gabia.weat.gcellapiserver.dto.MessageDto.*;

import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.converter.MessageDtoConverter;
import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageHandler {

	private final MessageSender messageSender;
	private final ExcelInfoRepository excelInfoRepository;

	@Transactional
	public void sendCreateExcelMsg(MessageWrapperDto<FileCreateProgressMsgDto> messageWrapperDto) {
		FileCreateProgressMsgDto message = messageWrapperDto.getMessage();
		switch(message.messageType()){
			case FILE_CREATION_PROGRESS -> messageSender.sendMessageToMemberId(message.memberId(), message.messageType(), message);
			case FILE_CREATION_COMPLETE -> this.sendCreateCompleteMsg(message);
		}
	}

	private void sendCreateCompleteMsg(FileCreateProgressMsgDto message){
		ExcelInfo excelInfo = excelInfoRepository.findById(message.excelInfoId()).orElseThrow(() -> {
			throw new CustomException(ErrorCode.EXCEL_NOT_EXISTS);
		});
		excelInfo.created();
		FileCreateCompleteMsgDto fileCreateCompleteMsgDto = MessageDtoConverter.createProgressMsgToCreateCompleteMsg(message, excelInfo.getName());
		messageSender.sendMessageToExcelInfoId(message.excelInfoId(), message.messageType(), fileCreateCompleteMsgDto);
	}

}