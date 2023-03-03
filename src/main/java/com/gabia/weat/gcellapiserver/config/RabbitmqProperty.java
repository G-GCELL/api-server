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
	}

	@Getter
	@AllArgsConstructor
	public static class QueueProperty {
		private String fileCreateRequestQueue;
		private String fileCreateProgressQueue;

		public String getFileCreateProgressQueue(String serverName) {
			return fileCreateProgressQueue + "-" + serverName.substring(serverName.length() - 1);
		}
	}

	@Getter
	@AllArgsConstructor
	public static class RoutingKeyProperty {
		private String fileCreateRequestRoutingKey;
	}

}
