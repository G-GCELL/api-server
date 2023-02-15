package com.gabia.weat.gcellapiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.gabia.weat.gcellapiserver.domain.type.MessageType;

import lombok.Builder;

public class MessageDto {

	@Builder
	public record FileCreateRequestMsgDto(
		Long memberId,
		String fileName,
		String[] inAccountId,
		String[] notAccountId,
		String[] inProductCode,
		String[] notProductCode,
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
		MessageType messageType,
		String memberFileName,
		Integer progressRate
	) {

	}

}