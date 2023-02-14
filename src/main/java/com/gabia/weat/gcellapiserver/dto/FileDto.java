package com.gabia.weat.gcellapiserver.dto;

import java.time.LocalDateTime;

import com.gabia.weat.gcellapiserver.repository.enums.CreatedAtCondition;
import com.gabia.weat.gcellapiserver.repository.enums.IdCondition;
import com.gabia.weat.gcellapiserver.repository.enums.NameCondition;

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

	@Builder
	public record FileUpdateNameRequestDto(
		String fileName
	) {

	}

	@Builder
	public record FileUpdateNameResponseDto(Long id, String fileName, boolean isDelete, LocalDateTime createdAt) {

	}

	public record FileListRequestDto(
		Long excelInfoId,
		IdCondition idCondition,
		String fileName,
		NameCondition nameCondition,
		LocalDateTime createdAt,
		CreatedAtCondition createdAtCondition,
		Boolean isDelete
	){

	}

}