package com.gabia.weat.gcellapiserver.config;

import java.util.Objects;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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
import com.gabia.weat.gcellapiserver.error.CustomRejectingErrorHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableRabbit
@RequiredArgsConstructor
public class RabbitmqConfig {

	@Value("${server.name}")
	private String serverName;
	private final RabbitmqProperty property;
	private final CustomRejectingErrorHandler errorHandler;

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
		return connectionFactory -> serverName;
	}

	@Bean
	RabbitAdmin rabbitAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	FanoutExchange fileCreateProgressExchange() {
		return new FanoutExchange(property.getExchange().getFileCreateProgressExchange(), true, false);
	}

	@Bean
	FanoutExchange fileCreateErrorExchange() {
		return new FanoutExchange(property.getExchange().getFileCreateErrorExchange(), true, false);
	}

	@Bean
	Queue fileCreateProgressQueue() {
		return new Queue(property.getQueue().getFileCreateProgressQueue(serverName), true);
	}

	@Bean
	Queue fileCreateErrorQueue() {
		return new Queue(property.getQueue().getFileCreateErrorQueue(serverName), true);
	}

	@Bean
	Binding fileCreateProgressBinding() {
		return BindingBuilder.bind(fileCreateProgressQueue())
			.to(fileCreateProgressExchange());
	}

	@Bean
	Binding fileCreateErrorBinding() {
		return BindingBuilder.bind(fileCreateErrorQueue())
			.to(fileCreateErrorExchange());
	}

	@Bean
	SimpleRabbitListenerContainerFactory fileCreateProgressListenerFactory() {
		SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		listenerContainerFactory.setConnectionFactory(connectionFactory());
		listenerContainerFactory.setMessageConverter(messageConverter());
		listenerContainerFactory.setContainerCustomizer(
			container -> container.setQueues(fileCreateProgressQueue())
		);
		listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO);
		listenerContainerFactory.setErrorHandler(errorHandler);
		listenerContainerFactory.setDefaultRequeueRejected(false);
		return listenerContainerFactory;
	}

	@Bean
	SimpleRabbitListenerContainerFactory fileCreateErrorListenerFactory() {
		SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
		listenerContainerFactory.setConnectionFactory(connectionFactory());
		listenerContainerFactory.setMessageConverter(messageConverter());
		listenerContainerFactory.setContainerCustomizer(
			container -> container.setQueues(fileCreateErrorQueue())
		);
		listenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO);
		listenerContainerFactory.setErrorHandler(errorHandler);
		listenerContainerFactory.setDefaultRequeueRejected(false);
		return listenerContainerFactory;
	}

	@Bean
	RabbitTemplate fileCreateRequestRabbitTemplate() {
		return this.getRabbitTemplate(
			property.getExchange().getDirectExchange(),
			property.getRoutingKey().getFileCreateRequestRoutingKey()
		);
	}

	@Bean
	RabbitTemplate csvUpdateRequestRabbitTemplate(){
		return this.getRabbitTemplate(
			property.getExchange().getDirectExchange(),
			property.getRoutingKey().getCsvUpdateRequestRoutingKey()
		);
	}

	private RabbitTemplate getRabbitTemplate(String exchangeName, String routingKeyName) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(messageConverter());
		rabbitTemplate.setExchange(exchangeName);
		rabbitTemplate.setRoutingKey(routingKeyName);
		rabbitTemplate.setMandatory(true);

		// 임시 코드
		rabbitTemplate.setReturnsCallback(returned -> {
			log.info("[반환된 메시지] " + returned);
		});

		// 임시 코드
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			if (!ack || Objects.requireNonNull(correlationData).getReturned() != null) {
				log.error("[메시지 발행 실패] " + cause);
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