package com.gabia.weat.gcellapiserver.dto;

import com.gabia.weat.gcellapiserver.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class APIResponseDTO<T> {

	private boolean success;
	private T response;
	private ErrorCode error;

	public static APIResponseDTO success() {
		return new APIResponseDTO(true, null, null);
	}

	public static <T> APIResponseDTO success(T response) {
		return new APIResponseDTO(true, response, null);
	}

	public static APIResponseDTO fail(ErrorCode errorCode) {
		return new APIResponseDTO(false, null, errorCode);
	}

}