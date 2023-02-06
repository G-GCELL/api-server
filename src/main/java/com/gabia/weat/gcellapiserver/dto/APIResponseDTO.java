package com.gabia.weat.gcellapiserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class APIResponseDTO<T> {

	private boolean success;
	private T response;
	private ErrorDTO error;

	public static APIResponseDTO success(){
		return new APIResponseDTO(true, null, null);
	}

	public static <T> APIResponseDTO success(T response){
		return new APIResponseDTO(true, response, null);
	}

	public static APIResponseDTO fail(ErrorDTO error){
		return new APIResponseDTO(false, null, error);
	}

}