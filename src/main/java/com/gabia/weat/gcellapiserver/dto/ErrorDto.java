package com.gabia.weat.gcellapiserver.dto;

import com.gabia.weat.gcellapiserver.error.CustomStatus;

public record ErrorDto(
	int status,
	String message
) {

}