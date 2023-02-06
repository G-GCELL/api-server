package com.gabia.weat.gcellapiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDTO {

	private Integer status;
	private String message;

}