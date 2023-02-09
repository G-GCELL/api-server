package com.gabia.weat.gcellapiserver.dto;

import java.time.LocalDateTime;

import lombok.Builder;

public class FileDto {

	public record FileCreateRequestDto(
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

	public record FileUpdateNameRequestDto(
		String fileName
	) {

	}

	@Builder
	public record FileUpdateNameResponseDto(Long id, String fileName, boolean isDelete, LocalDateTime createdAt) {

	}

}