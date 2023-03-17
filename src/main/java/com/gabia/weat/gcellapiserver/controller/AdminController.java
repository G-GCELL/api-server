package com.gabia.weat.gcellapiserver.controller;

import static com.gabia.weat.gcellapiserver.dto.FileDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.weat.gcellapiserver.dto.ApiResponseDto;
import com.gabia.weat.gcellapiserver.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/dataset")
	public ResponseEntity<ApiResponseDto> uploadDataSet(CsvUpdateRequestDto csvUpdateRequestDto){
		adminService.updateCsvFile(csvUpdateRequestDto);
		return ResponseEntity.ok(ApiResponseDto.success());
	}

}
