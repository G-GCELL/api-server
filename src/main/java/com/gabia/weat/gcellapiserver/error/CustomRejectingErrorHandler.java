package com.gabia.weat.gcellapiserver.error;

import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellapiserver.service.log.LogPrinter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomRejectingErrorHandler extends ConditionalRejectingErrorHandler {

	private final LogPrinter logPrinter;

	@Override
	protected void log(Throwable t) {
		logPrinter.printErrorLog((Exception) t.getCause());
	}

}