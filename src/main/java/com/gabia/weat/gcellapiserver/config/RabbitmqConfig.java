package com.gabia.weat.gcellapiserver.config;

import java.util.Objects;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableRabbit
public class RabbitmqConfig {

	@Value("${spring.application.name}")
	private String applicationName;
	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	@Value("${rabbitmq.direct-exchange}")
	private String directExchange;
	@Value("${rabbitmq.file-create-progress-exchange}")
	private String fileCreateProgressExchange;
	@Value("${rabbitmq.file-create-request-queue}")
	private String fileCreateRequestQueue;
	@Value("${rabbitmq.file-create-request-routing-key}")
	private String fileCreateRequestRoutingKey;

	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		connectionFactory.setPublisherReturns(true);
		connectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
		connectionFactory.setConnectionNameStrategy(connectionNameStrategy());
		return connectionFactory;
	}

	@Bean
	ConnectionNameStrategy connectionNameStrategy() {
		return connectionFactory -> applicationName;
	}

	@Bean
	RabbitAdmin rabbitAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	Queue fileCreateRequestQueue() {
		return new Queue(fileCreateRequestQueue, true);
	}

	@Bean
	Queue fileCreateProgressQueue() {
		return new Queue(applicationName.toLowerCase(), true);
	}

	@Bean
	DirectExchange directExchange() {
		return new DirectExchange(directExchange, true, false);
	}

	@Bean
	FanoutExchange fileCreateProgressExchange() {
		return new FanoutExchange(fileCreateProgressExchange, true, false);
	}

	@Bean
	Declarables fileCreateRequestBindings() {
		return new Declarables(
			BindingBuilder.bind(fileCreateRequestQueue()).to(directExchange()).with(fileCreateRequestRoutingKey)
		);
	}

	@Bean
	Declarables fileCreateProgressBindings() {
		return new Declarables(
			BindingBuilder.bind(fileCreateProgressQueue()).to(fileCreateProgressExchange())
		);
	}

	@Bean
	SimpleRabbitListenerContainerFactory fileCreateProgressListenerFactory() {
		SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		listenerContainerFactory.setConnectionFactory(connectionFactory());
		listenerContainerFactory.setMessageConverter(messageConverter());
		listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		return listenerContainerFactory;
	}

	@Bean
	RabbitTemplate fileCreateRequestRabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(messageConverter());
		rabbitTemplate.setExchange(directExchange);
		rabbitTemplate.setRoutingKey(fileCreateRequestRoutingKey);
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
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		return new Jackson2JsonMessageConverter(objectMapper);
	}

}