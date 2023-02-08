package com.gabia.weat.gcellapiserver.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gabia.weat.gcellapiserver.dto.ApiResponseDto;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	private ResponseEntity<ApiResponseDto> customExceptionHandler(CustomException exception) {
		return ResponseEntity.status(exception.getErrorCode().getStatus())
			.body(ApiResponseDto.fail(exception.getErrorCode()));
	}

}