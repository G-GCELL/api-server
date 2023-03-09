package com.gabia.weat.gcellapiserver.controller;

import static com.gabia.weat.gcellapiserver.dto.FileDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.weat.gcellapiserver.dto.ApiResponseDto;
import com.gabia.weat.gcellapiserver.service.ManagerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ManagerController {

	private final ManagerService managerService;

	@PostMapping("/dataset")
	public ResponseEntity<ApiResponseDto> uploadDataSet(CsvUpdateRequestDto csvUpdateRequestDto){
		managerService.updateCsvFile(csvUpdateRequestDto);
		return ResponseEntity.ok(ApiResponseDto.success());
	}

}