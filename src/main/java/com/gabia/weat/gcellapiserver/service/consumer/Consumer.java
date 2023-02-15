package com.gabia.weat.gcellapiserver.service.consumer;

import java.io.IOException;

import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import com.rabbitmq.client.Channel;

public interface Consumer<T> {

	void receiveMessage(T message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException;

}
