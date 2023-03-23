package com.gabia.weat.gcellapiserver.converter;

import com.gabia.weat.gcellcommonmodule.dto.MessageDto.FileCreateCompleteMsgDto;
import com.gabia.weat.gcellcommonmodule.dto.MessageDto.FileCreateProgressMsgDto;

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