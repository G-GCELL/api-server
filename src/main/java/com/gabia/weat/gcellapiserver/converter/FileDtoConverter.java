package com.gabia.weat.gcellapiserver.converter;

import java.math.BigDecimal;

import com.gabia.weat.gcellapiserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateRequestMsgDto;

public class FileDtoConverter {

	public static FileCreateRequestMsgDto createDtoToCreateMsgDto(Long memberId,
		String newFileName,
		FileCreateRequestDto fileCreateRequestDto) {
		BigDecimal costMin =
			fileCreateRequestDto.costMin() == null ? null : new BigDecimal(fileCreateRequestDto.costMin());
		BigDecimal costMax =
			fileCreateRequestDto.costMax() == null ? null : new BigDecimal(fileCreateRequestDto.costMax());

		return FileCreateRequestMsgDto.builder()
			.memberId(memberId)
			.fileName(newFileName)
			.inAccountId(fileCreateRequestDto.inAccountId())
			.notAccountId(fileCreateRequestDto.notAccountId())
			.inProductCode(fileCreateRequestDto.inProductCode())
			.notProductCode(fileCreateRequestDto.notProductCode())
			.startDateMin(fileCreateRequestDto.startDateMin())
			.startDateMax(fileCreateRequestDto.startDateMax())
			.endDateMin(fileCreateRequestDto.endDateMin())
			.endDateMax(fileCreateRequestDto.endDateMax())
			.costMin(costMin)
			.costMax(costMax)
			.build();
	}

}