package com.gabia.weat.gcellapiserver.service.consumer;

import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;

public interface Consumer<T> {

	void receiveMessage(MessageWrapperDto<T> message) throws Exception;

}