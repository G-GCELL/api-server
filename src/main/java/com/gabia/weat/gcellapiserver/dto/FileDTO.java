package com.gabia.weat.gcellapiserver.dto;

import java.time.LocalDateTime;

public class FileDTO {

	public record FileCreateRequestDTO(
		String fileName,
		String[] inAccountId,
		String[] notAccountId,
		String[] inProductCode,
		String[] notProductCode,
		LocalDateTime startDateMin,
		LocalDateTime startDateMax,
		LocalDateTime endDateMin,
		LocalDateTime endDateMax,
		String costMin,
		String costMax
	) {

	}

}