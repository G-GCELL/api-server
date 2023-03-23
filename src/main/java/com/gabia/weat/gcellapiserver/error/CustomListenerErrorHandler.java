package com.gabia.weat.gcellapiserver.error;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellcommonmodule.error.ErrorCode;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;
import com.gabia.weat.gcellcommonmodule.printer.LogPrinter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomListenerErrorHandler implements RabbitListenerErrorHandler {

	private final LogPrinter logPrinter;

	@Override
	public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message,
		ListenerExecutionFailedException exception) throws CustomException {
		Throwable t = exception.getCause();
		logPrinter.printErrorLog((Exception) t);
		ErrorCode errorCode = ErrorCode.UNKNOWN_ERROR;

		if (t instanceof CustomException ce) {
			errorCode = ce.getErrorCode();
		}

		throw new CustomException(t, errorCode);
	}

}