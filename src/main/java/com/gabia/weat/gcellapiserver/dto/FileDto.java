package com.gabia.weat.gcellapiserver.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.gabia.weat.gcellapiserver.repository.enums.IdCondition;
import com.gabia.weat.gcellapiserver.repository.enums.NameCondition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

public class FileDto {

	public record FileCreateRequestDto(
		@NotBlank
		String fileName,
		String[] inAccountId,
		String[] notAccountId,
		String[] inProductCode,
		String[] notProductCode,
		@NotBlank
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime startDateMin,
		@NotBlank
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime startDateMax,
		@NotBlank
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime endDateMin,
		@NotBlank
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime endDateMax,
		@NotBlank
		String costMin,
		@NotBlank
		String costMax
	) {

	}

	@Builder
	public record FileUpdateNameRequestDto(
		@NotBlank
		String fileName
	) {

	}

	@Builder
	public record FileUpdateNameResponseDto(
		Long id,
		String fileName,
		boolean isDelete,
		LocalDateTime createdAt
	) {

	}

	@Builder
	public record FileListRequestDto(
		List<Long> excelInfoIdList,
		IdCondition idCondition,
		String fileName,
		NameCondition nameCondition,
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime minCreatedAt,
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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