package com.gabia.weat.gcellapiserver.dto;

import com.gabia.weat.gcellapiserver.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class APIResponseDto<T> {

	private boolean success;
	private T response;
	private ErrorDto error;

	public static APIResponseDto success() {
		return new APIResponseDto(true, null, null);
	}

	public static <T> APIResponseDto success(T response) {
		return new APIResponseDto(true, response, null);
	}

	public static APIResponseDto fail(ErrorCode errorCode) {
		return new APIResponseDto(false, null, new ErrorDto(errorCode.getCode().getStatus(), errorCode.getMessage()));
	}

}