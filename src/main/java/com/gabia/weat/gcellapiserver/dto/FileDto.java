package com.gabia.weat.gcellapiserver.dto;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.weat.gcellapiserver.domain.type.ExcelStatusType;
import com.gabia.weat.gcellapiserver.repository.enums.IdCondition;
import com.gabia.weat.gcellapiserver.repository.enums.NameCondition;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;

public class FileDto {

	public record FileCreateRequestDto(
		@NotBlank
		String fileName,
		@NotEmpty
		List<String> columnNames,
		List<String> inAccountId,
		List<String> notAccountId,
		List<String> inProductCode,
		List<String> notProductCode,
		@NotNull
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime startDateMin,
		@NotNull
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime startDateMax,
		@NotNull
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
		LocalDateTime endDateMin,
		@NotNull
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
		ExcelStatusType status,
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
		ExcelStatusType status
	) {

	}

	public record FileListResponseDto(
		Long excelInfoId,
		String fileName,
		LocalDateTime createdAt,
		ExcelStatusType status
	) {

	}

	public record CsvUpdateRequestDto(
		@NotNull
		MultipartFile file,
		YearMonth deleteTarget
	) {

	}

}