package com.gabia.weat.gcellapiserver.error;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellapiserver.error.exception.CustomException;

@Component
public class CustomListenerErrorHandler implements RabbitListenerErrorHandler {

	@Override
	public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message,
		ListenerExecutionFailedException exception) throws Exception {
		Throwable t = exception.getCause();
		ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;

		if (t instanceof CustomException ce) {
			errorCode = ce.getErrorCode();
		}

		throw new CustomException(t, errorCode);
	}

}
