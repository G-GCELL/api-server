package com.gabia.weat.gcellapiserver.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import static com.gabia.weat.gcellapiserver.dto.FileDTO.FileCreateRequestDTO;

@Service
@RequiredArgsConstructor
public class ExcelInfoService {

	public Long createExcel(String email, FileCreateRequestDTO fileCreateRequestDTO){
		return null;
	}

}