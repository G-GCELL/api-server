package com.gabia.weat.gcellapiserver.config;

import java.util.Objects;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	private static final String EXCHANGE_NAME = "gcell";

	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setPublisherReturns(true);
		connectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
		return connectionFactory;
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(EXCHANGE_NAME);
		rabbitTemplate.setMessageConverter(messageConverter());
		rabbitTemplate.setMandatory(true);

		rabbitTemplate.setReturnsCallback(returned -> {
			System.out.println("반환된 메시지 : " + returned);
		});

		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			if (ack && Objects.requireNonNull(correlationData).getReturned() == null) {
				System.out.println("메시지 발행 성공");
			} else {
				System.out.println("메시지 발행 실패");
				System.out.println("원인 : " + cause);
			}
		});

		return rabbitTemplate;
	}

	@Bean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

}