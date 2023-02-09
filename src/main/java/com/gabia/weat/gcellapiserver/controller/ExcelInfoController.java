package com.gabia.weat.gcellapiserver.controller;

import static com.gabia.weat.gcellapiserver.dto.FileDto.FileCreateRequestDto;
import static com.gabia.weat.gcellapiserver.dto.FileDto.FileUpdateNameRequestDto;
import static com.gabia.weat.gcellapiserver.dto.FileDto.FileUpdateNameResponseDto;

import com.gabia.weat.gcellapiserver.converter.FileDtoConverter;
import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.dto.FileDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gabia.weat.gcellapiserver.dto.ApiResponseDto;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.service.ExcelInfoService;
import com.gabia.weat.gcellapiserver.service.MinioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/excels")
public class ExcelInfoController {

	private final ExcelInfoService excelInfoService;
	private final MinioService minioService;

	@PostMapping(value = "")
	public ResponseEntity<ApiResponseDto> createExcel(@RequestBody FileCreateRequestDto fileCreateRequestDto) {
		Long createExcelInfoId = excelInfoService.createExcel(this.getConnectMemberEmail(), fileCreateRequestDto);
		String downloadUrl = "/excels/" + createExcelInfoId;
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseDto.success(downloadUrl));
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public byte[] downloadExcel(@PathVariable("id") Long excelInfoId) {
		byte[] excel = minioService.downloadExcel(excelInfoId, this.getConnectMemberEmail());
		return excel;
	}

	@PatchMapping(value = "{id}")
	public ResponseEntity<ApiResponseDto> updateExcelName(@RequestBody FileUpdateNameRequestDto fileUpdateNameRequestDto, @PathVariable("id") Long excelInfoId){
		FileUpdateNameResponseDto fileUpdateNameResponseDto = excelInfoService.updateExcelInfoName(this.getConnectMemberEmail(), excelInfoId, fileUpdateNameRequestDto);
		return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDto.success(fileUpdateNameResponseDto));
	}

	private String getConnectMemberEmail() {
		return "mock_email";
	}

}