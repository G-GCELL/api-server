package com.gabia.weat.gcellapiserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitmqProperty {

	private String host;
	private int port;
	private String username;
	private String password;
	private ExchangeProperty exchange;
	private QueueProperty queue;
	private RoutingKeyProperty routingKey;

	@Getter
	@AllArgsConstructor
	public static class ExchangeProperty {
		private String directExchange;
		private String fileCreateProgressExchange;
		private String fileCreateErrorExchange;
	}

	@Getter
	@AllArgsConstructor
	public static class QueueProperty {
		private String fileCreateProgressQueue;
		private String fileCreateErrorQueue;

		public String getFileCreateProgressQueue(String serverName) {
			return fileCreateProgressQueue + "-" + serverName.substring(serverName.length() - 1);
		}

		public String getFileCreateErrorQueue(String serverName) {
			return fileCreateErrorQueue + "-" + serverName.substring(serverName.length() - 1);
		}
	}

	@Getter
	@AllArgsConstructor
	public static class RoutingKeyProperty {
		private String fileCreateRequestRoutingKey;
		private String CsvUpdateRequestRoutingKey;
	}

}