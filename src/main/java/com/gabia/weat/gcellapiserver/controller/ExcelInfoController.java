package com.gabia.weat.gcellapiserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.weat.gcellapiserver.dto.APIResponseDTO;
import com.gabia.weat.gcellapiserver.service.ExcelInfoService;
import com.gabia.weat.gcellapiserver.service.MinioService;

import static com.gabia.weat.gcellapiserver.dto.FileDTO.FileCreateRequestDTO;

import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/excels")
public class ExcelInfoController {

	private final ExcelInfoService excelInfoService;

	private final MinioService minioService;

	@PostMapping(value = "")
	@ResponseStatus(HttpStatus.CREATED)
	public APIResponseDTO createExcel(@RequestBody FileCreateRequestDTO fileCreateRequestDTO) {
		Long createExcelInfoId = excelInfoService.createExcel(this.getConnectMemberEmail(), fileCreateRequestDTO);
		String downloadUrl = "/excels/" + createExcelInfoId;
		return APIResponseDTO.success(downloadUrl);
	}

	@GetMapping(value = "/{id}")
	public APIResponseDTO<Byte[]> downloadExcel(@PathVariable("id") Long excelInfoId) throws
		CustomException {
		byte[] excel = minioService.downloadExcel(excelInfoId, this.getConnectMemberEmail());
		return APIResponseDTO.success(excel);
	}

	private String getConnectMemberEmail() {
		return "mock_email";
	}
}