package com.gabia.weat.gcellapiserver.parser;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootTest(classes = {
	Environment.class,
	SpELExpressionEnvParserTest.TestExpressionParserConfig.class
})
public class SpELExpressionEnvParserTest {

	@Autowired
	private CustomExpressionParser expressionParser;

	@Test
	@DisplayName("SpEL 표현식 파싱 테스트")
	public void parse_test() {
		// given
		String spEL = "${rabbitmq.exchange.direct-exchange}";

		// when
		String value = (String)expressionParser.parse(spEL);

		// then
		assertThat(value).isEqualTo("exchange");

	}

	@TestConfiguration
	public static class TestExpressionParserConfig{

		@Autowired
		private Environment environment;

		@Bean
		public CustomExpressionParser expressionBeanParser(){
			return new SpELExpressionEnvParser(environment);
		}

	}

}
