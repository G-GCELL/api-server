package com.gabia.weat.gcellapiserver.service.producer;

import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;

public interface Producer<T> {

	void sendMessage(MessageWrapperDto<T> message);

}