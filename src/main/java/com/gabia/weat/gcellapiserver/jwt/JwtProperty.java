package com.gabia.weat.gcellapiserver.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
public class JwtProperty {

	private String secret;
	private long accessTokenExpiration;

}
