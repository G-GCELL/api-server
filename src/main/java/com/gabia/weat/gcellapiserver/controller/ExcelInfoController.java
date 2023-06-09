package com.gabia.weat.gcellapiserver.controller;

import static com.gabia.weat.gcellapiserver.dto.FileDto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.weat.gcellapiserver.dto.ApiResponseDto;
import com.gabia.weat.gcellapiserver.service.ExcelInfoService;
import com.gabia.weat.gcellapiserver.service.MinioService;
import com.gabia.weat.gcellcommonmodule.annotation.ControllerLog;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@ControllerLog
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/excels")
public class ExcelInfoController {

	private final ExcelInfoService excelInfoService;
	private final MinioService minioService;

	@PostMapping(value = "")
	public ResponseEntity<ApiResponseDto> createExcel(@RequestBody @Valid FileCreateRequestDto fileCreateRequestDto) {
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

	@PatchMapping(value = "/{id}")
	public ResponseEntity<ApiResponseDto> updateExcelName(
		@RequestBody @Valid FileUpdateNameRequestDto fileUpdateNameRequestDto, @PathVariable("id") Long excelInfoId) {
		FileUpdateNameResponseDto fileUpdateNameResponseDto = excelInfoService.updateExcelInfoName(
			this.getConnectMemberEmail(), excelInfoId, fileUpdateNameRequestDto);
		return ResponseEntity.ok(ApiResponseDto.success(fileUpdateNameResponseDto));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ApiResponseDto> deleteExcelInfo(@PathVariable("id") Long excelInfoId) {
		excelInfoService.deleteExcelInfo(this.getConnectMemberEmail(), excelInfoId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponseDto.success());
	}

	@GetMapping
	public ResponseEntity<ApiResponseDto> getExcelInfoList(@ModelAttribute @Valid FileListRequestDto fileListRequestDto,
		Pageable pageable) {
		Page<FileListResponseDto> excelInfos = excelInfoService.getExcelInfo(getConnectMemberEmail(), pageable,
			fileListRequestDto);
		return ResponseEntity.ok(ApiResponseDto.success(excelInfos));
	}

	private String getConnectMemberEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}