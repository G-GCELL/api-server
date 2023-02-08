package com.gabia.weat.gcellapiserver.error.exception;

public class SseNotConnectException extends RuntimeException {

	private Integer status;

	public SseNotConnectException() {
		super();
	}

	public SseNotConnectException(Throwable th) {
		super(th);
	}

	public SseNotConnectException(Integer status, String message) {
		super(message);
		this.status = status;
	}

}