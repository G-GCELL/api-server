package com.gabia.weat.gcellapiserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.weat.gcellapiserver.dto.APIResponseDto;
import com.gabia.weat.gcellapiserver.service.ExcelInfoService;
import com.gabia.weat.gcellapiserver.service.MinioService;

import static com.gabia.weat.gcellapiserver.dto.FileDto.FileCreateRequestDto;

import com.gabia.weat.gcellapiserver.error.exception.CustomException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/excels")
public class ExcelInfoController {

	private final ExcelInfoService excelInfoService;
	private final MinioService minioService;

	@PostMapping(value = "")
	public ResponseEntity<APIResponseDto> createExcel(@RequestBody FileCreateRequestDto fileCreateRequestDto) {
		Long createExcelInfoId = excelInfoService.createExcel(this.getConnectMemberEmail(), fileCreateRequestDto);
		String downloadUrl = "/excels/" + createExcelInfoId;
		return ResponseEntity.status(HttpStatus.CREATED).body(APIResponseDto.success(downloadUrl));
	}

	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public byte[] downloadExcel(@PathVariable("id") Long excelInfoId) throws
		CustomException {
		byte[] excel = minioService.downloadExcel(excelInfoId, this.getConnectMemberEmail());
		return excel;
	}

	private String getConnectMemberEmail() {
		return "mock_email";
	}
}