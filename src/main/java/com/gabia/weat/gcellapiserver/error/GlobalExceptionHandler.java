package com.gabia.weat.gcellapiserver.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gabia.weat.gcellapiserver.dto.ApiResponseDto;
import com.gabia.weat.gcellapiserver.dto.ErrorDto;
import com.gabia.weat.gcellcommonmodule.error.ErrorCode;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	private ResponseEntity<ApiResponseDto> customExceptionHandler(CustomException exception) {
		ErrorCode errorCode = exception.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus())
			.body(ApiResponseDto.fail(errorCode));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponseDto> validationExceptionHandler(MethodArgumentNotValidException exception) {
		return ResponseEntity.status(exception.getStatusCode())
			.body(ApiResponseDto.fail(new ErrorDto(exception.getStatusCode().value(),
				exception.getBindingResult().getAllErrors().get(0).getDefaultMessage())));
	}

}