package com.gabia.weat.gcellapiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.gabia.weat.gcellapiserver.domain.type.MessageType;

import lombok.Builder;
import lombok.Setter;

public class MessageDto {

	@Builder
	public record FileCreateRequestMsgDto(
		Long memberId,
		Long excelInfoId,
		String fileName,
		List<String> columnNames,
		List<String> inAccountId,
		List<String> notAccountId,
		List<String> inProductCode,
		List<String> notProductCode,
		LocalDateTime startDateMin,
		LocalDateTime startDateMax,
		LocalDateTime endDateMin,
		LocalDateTime endDateMax,
		BigDecimal costMin,
		BigDecimal costMax
	) {

	}

	public record FileCreateProgressMsgDto(
		Long memberId,
		Long excelInfoId,
		MessageType messageType,
		String memberFileName,
		Integer progressRate
	) {

	}

	public record FileCreateCompleteMsgDto(
		Long memberId,
		Long excelInfoId,
		String fileName
	){

	}

}