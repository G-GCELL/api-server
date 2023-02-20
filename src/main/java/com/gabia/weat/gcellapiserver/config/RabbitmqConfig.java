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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitmqConfig {

	@Value("${spring.application.name}")
	private String applicationName;
	private final RabbitmqConfigProperty property;

	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(property.getHost());
		connectionFactory.setPort(property.getPort());
		connectionFactory.setUsername(property.getUsername());
		connectionFactory.setPassword(property.getPassword());
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
		return new Queue(property.getQueue().getFileCreateRequestQueue(), true);
	}

	@Bean
	Queue fileCreateProgressQueue() {
		return new Queue(property.getQueue().getFileCreateProgressQueue(applicationName), true);
	}

	@Bean
	DirectExchange directExchange() {
		return new DirectExchange(property.getExchange().getDirectExchange(), true, false);
	}

	@Bean
	FanoutExchange fileCreateProgressExchange() {
		return new FanoutExchange(property.getExchange().getFileCreateProgressExchange(), true, false);
	}

	@Bean
	Declarables fileCreateRequestBindings() {
		return new Declarables(
			BindingBuilder.bind(fileCreateRequestQueue()).to(directExchange()).with(property.getRoutingKey().getFileCreateRequestRoutingKey())
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
		rabbitTemplate.setExchange(property.getExchange().getDirectExchange());
		rabbitTemplate.setRoutingKey(property.getRoutingKey().getFileCreateRequestRoutingKey());
		rabbitTemplate.setMandatory(true);

		// 임시 코드
		rabbitTemplate.setReturnsCallback(returned -> {
			log.info("[반환된 메시지] " + returned);
		});

		// 임시 코드
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