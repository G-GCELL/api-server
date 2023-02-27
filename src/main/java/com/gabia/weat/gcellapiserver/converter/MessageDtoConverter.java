package com.gabia.weat.gcellapiserver.converter;

import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateCompleteMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateProgressMsgDto;

public class MessageDtoConverter {

	public static FileCreateCompleteMsgDto createProgressMsgToCreateCompleteMsg(
		FileCreateProgressMsgDto createProgressMsgDto, String fileName) {
		return new FileCreateCompleteMsgDto(
			createProgressMsgDto.memberId(),
			createProgressMsgDto.excelInfoId(),
			fileName
		);
	}

}