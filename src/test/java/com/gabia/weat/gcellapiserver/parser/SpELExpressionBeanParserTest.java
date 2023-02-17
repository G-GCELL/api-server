package com.gabia.weat.gcellapiserver.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.gabia.weat.gcellapiserver.config.ExpressionBeanParserConfig;

@SpringBootTest(classes = {ExpressionBeanParserConfig.class, SpELExpressionBeanParserTest.TestBeanConfig.class})
public class SpELExpressionBeanParserTest {

	@Autowired
	private CustomExpressionBeanParser expressionBeanParser;

	@Test
	@DisplayName("SpEL 표현식 파싱 테스트")
	public void parse_test() {
		// given
		String spEL = "@testBean.getValue()";

		// when
		String value = (String)expressionBeanParser.parse(spEL);

		// then
		assertThat(value).isEqualTo("test");

	}

	@TestConfiguration
	public static class TestBeanConfig {
		@Bean
		public TestBean testBean() {
			return new TestBean();
		}
	}

	static class TestBean {

		public String getValue() {
			return "test";
		}

	}

}