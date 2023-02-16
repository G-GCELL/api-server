package com.gabia.weat.gcellapiserver.dto;

import java.time.LocalDateTime;
import java.util.List;

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

	@Builder
	public record FileListRequestDto(
		List<Long> excelInfoIdList,
		IdCondition idCondition,
		String fileName,
		NameCondition nameCondition,
		LocalDateTime minCreatedAt,
		LocalDateTime maxCreatedAt,
		Boolean isDelete
	) {

	}

	public record FileListResponseDto(
		Long excelInfoId,
		String fileName,
		LocalDateTime createdAt,
		Boolean isDelete
	) {

	}

}