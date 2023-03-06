package com.gabia.weat.gcellapiserver.error;

import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomRejectingErrorHandler extends ConditionalRejectingErrorHandler {

	@Override
	protected void log(Throwable t) {
		// 임시 코드
		// Consumer 로그 출력 역할에 대한 논의 필요.
	}

}
