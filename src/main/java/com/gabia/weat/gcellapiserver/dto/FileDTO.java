package com.gabia.weat.gcellapiserver.dto;

import java.time.LocalDate;

public class FileDTO {

	public record FileCreateRequestDTO(
		String fileName,
		String[] inAccountId,
		String[] notAccountId,
		String[] inProductCode,
		String[] notProductCode,
		LocalDate startDateMin,
		LocalDate startDateMax,
		LocalDate endDateMin,
		LocalDate endDateMax,
		String costMin,
		String costMax
	) {

	}

}