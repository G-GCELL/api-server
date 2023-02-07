package com.gabia.weat.gcellapiserver.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MessageDTO {

	public record FileCreateRequestMsgDTO(
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
}
