package com.gabia.weat.gcellapiserver.config;

import java.util.Objects;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RabbitmqConfig {

	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	@Value("${spring.rabbitmq.template.exchange}")
	private String exchange;
	@Value("${spring.rabbitmq.template.routing-key}")
	private String routingKey;

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
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setExchange(exchange);
		rabbitTemplate.setRoutingKey(routingKey);
		rabbitTemplate.setMessageConverter(messageConverter);
		rabbitTemplate.setMandatory(true);

		rabbitTemplate.setReturnsCallback(returned -> {
			log.info("[반환된 메시지] " + returned);
		});

		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			if (ack && Objects.requireNonNull(correlationData).getReturned() == null) {
				log.info("[메시지 발행 성공]");
			} else {
				log.info("[메시지 발행 실패] " + cause);
			}
		});

		return rabbitTemplate;
	}

	@Bean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

}