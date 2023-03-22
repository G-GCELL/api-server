package com.gabia.weat.gcellapiserver.dto;

import com.gabia.weat.gcellcommonmodule.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponseDto<T> {

	private boolean success;
	private T response;
	private ErrorDto error;

	public static ApiResponseDto success() {
		return new ApiResponseDto(true, null, null);
	}

	public static <T> ApiResponseDto success(T response) {
		return new ApiResponseDto(true, response, null);
	}

	public static ApiResponseDto fail(ErrorCode errorCode) {
		return new ApiResponseDto(false, null, new ErrorDto(errorCode.getCustomStatus().getCode(), errorCode.getMessage()));
	}

	public static ApiResponseDto fail(ErrorDto errorDto) {
		return new ApiResponseDto(false, null, errorDto);
	}

}