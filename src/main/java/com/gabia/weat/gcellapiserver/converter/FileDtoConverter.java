package com.gabia.weat.gcellapiserver.converter;

import java.math.BigDecimal;

import com.gabia.weat.gcellapiserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateRequestMsgDto;

public class FileDtoConverter {

	public static FileCreateRequestMsgDto createDtoToCreateMsgDto(Long memberId, FileCreateRequestDto fileCreateRequestDTO){
		return FileCreateRequestMsgDto.builder()
			.memberId(memberId)
			.fileName(fileCreateRequestDTO.fileName())
			.inAccountId(fileCreateRequestDTO.inAccountId())
			.notAccountId(fileCreateRequestDTO.notAccountId())
			.inProductCode(fileCreateRequestDTO.inProductCode())
			.notProductCode(fileCreateRequestDTO.notProductCode())
			.startDateMin(fileCreateRequestDTO.startDateMin())
			.startDateMax(fileCreateRequestDTO.startDateMax())
			.endDateMin(fileCreateRequestDTO.endDateMin())
			.endDateMax(fileCreateRequestDTO.endDateMax())
			.costMin(new BigDecimal(fileCreateRequestDTO.costMin()))
			.costMax(new BigDecimal(fileCreateRequestDTO.costMax()))
			.build();
	}

}