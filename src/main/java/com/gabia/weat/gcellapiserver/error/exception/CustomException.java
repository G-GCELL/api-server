package com.gabia.weat.gcellapiserver.error.exception;

import com.gabia.weat.gcellapiserver.error.ErrorCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomException extends RuntimeException {
	private final ErrorCode errorCode;

}
