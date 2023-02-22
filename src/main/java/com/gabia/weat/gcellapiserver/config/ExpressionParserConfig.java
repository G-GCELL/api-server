package com.gabia.weat.gcellapiserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.gabia.weat.gcellapiserver.parser.CustomExpressionParser;
import com.gabia.weat.gcellapiserver.parser.SpELExpressionEnvParser;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ExpressionParserConfig {

	private final Environment environment;

	@Bean
	public CustomExpressionParser expressionBeanParser(){
		return new SpELExpressionEnvParser(environment);
	}

}