package com.gabia.weat.gcellapiserver.converter;

import java.math.BigDecimal;

import static com.gabia.weat.gcellapiserver.dto.FileDto.FileUpdateNameResponseDto;
import static com.gabia.weat.gcellapiserver.dto.MessageDto.*;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.type.JobType;
import com.gabia.weat.gcellapiserver.dto.FileDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellapiserver.dto.FileDto.FileCreateRequestDto;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateRequestMsgDto;

public class FileDtoConverter {

	public static FileCreateRequestMsgDto createDtoToCreateMsgDto(
		Long memberId,
		Long excelInfoId,
		String newFileName,
		FileCreateRequestDto fileCreateRequestDto) {
		BigDecimal costMin =
			fileCreateRequestDto.costMin() == null ? null : new BigDecimal(fileCreateRequestDto.costMin());
		BigDecimal costMax =
			fileCreateRequestDto.costMax() == null ? null : new BigDecimal(fileCreateRequestDto.costMax());

		return FileCreateRequestMsgDto.builder()
			.memberId(memberId)
			.excelInfoId(excelInfoId)
			.fileName(newFileName)
			.columnNames(fileCreateRequestDto.columnNames())
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

	public static FileUpdateNameResponseDto createEntityToUpdateNameResponseDto(ExcelInfo excelInfo) {
		return FileUpdateNameResponseDto.builder()
			.id(excelInfo.getExcelInfoId())
			.createdAt(excelInfo.getCreatedAt())
			.fileName(excelInfo.getName())
			.status(excelInfo.getStatus())
			.build();
	}

	public static CsvUpdateRequestMsgDto updateReqDtoToUpdateReqMsgDto(CsvUpdateRequestDto csvUpdateRequestDto){
		return new CsvUpdateRequestMsgDto(
			csvUpdateRequestDto.file().getOriginalFilename(),
			csvUpdateRequestDto.deleteTarget(),
			JobType.MANUAL
		);
	}

}