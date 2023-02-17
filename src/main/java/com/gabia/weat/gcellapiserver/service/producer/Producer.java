package com.gabia.weat.gcellapiserver.service.producer;

public interface Producer<T> {

	void sendMessage(T message);

}