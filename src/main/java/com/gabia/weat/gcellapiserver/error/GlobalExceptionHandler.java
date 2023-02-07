package com.gabia.weat.gcellapiserver.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gabia.weat.gcellapiserver.dto.APIResponseDTO;

import io.minio.errors.MinioException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MinioException.class)
	private APIResponseDTO minioException(ErrorCode errorCode) {
		return APIResponseDTO.fail(errorCode);
	}
}
