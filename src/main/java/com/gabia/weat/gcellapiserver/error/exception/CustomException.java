package com.gabia.weat.gcellapiserver.error.exception;

import com.gabia.weat.gcellapiserver.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

	private final ErrorCode errorCode;

}