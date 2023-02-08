package com.gabia.weat.gcellapiserver.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gabia.weat.gcellapiserver.dto.APIResponseDto;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	private ResponseEntity<APIResponseDto> customExceptionHandler(CustomException e) {
		return ResponseEntity.status(e.getErrorCode().getStatus()).body(APIResponseDto.fail(e.getErrorCode()));
	}

}