package com.gabia.weat.gcellapiserver.controller;

import static com.gabia.weat.gcellapiserver.dto.FileDto.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabia.weat.gcellapiserver.dto.ApiResponseDto;
import com.gabia.weat.gcellapiserver.service.AdminService;
import com.gabia.weat.gcellcommonmodule.annotation.ControllerLog;

import lombok.RequiredArgsConstructor;

@ControllerLog
@RestController
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@PostMapping("/dataset")
	public ResponseEntity<ApiResponseDto> uploadDataSet(CsvUpdateRequestDto csvUpdateRequestDto){
		adminService.updateCsvFile(this.getConnectMemberEmail(), csvUpdateRequestDto);
		return ResponseEntity.ok(ApiResponseDto.success());
	}

	private String getConnectMemberEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}