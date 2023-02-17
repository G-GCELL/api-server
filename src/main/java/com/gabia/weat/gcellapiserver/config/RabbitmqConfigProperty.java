package com.gabia.weat.gcellapiserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitmqConfigProperty {

	private String host;
	private int port;
	private String username;
	private String password;
	private String directExchange;
	private String fileCreateProgressExchange;
	private String fileCreateRequestQueue;
	private String fileCreateProgressQueue;
	private String fileCreateRequestRoutingKey;

	public String getFileCreateProgressQueue(String applicationName) {
		return fileCreateProgressQueue + "-" + applicationName.substring(applicationName.length() - 1);
	}

}
