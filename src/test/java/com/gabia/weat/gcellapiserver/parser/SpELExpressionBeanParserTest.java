package com.gabia.weat.gcellapiserver.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;


@SpringBootTest(classes = {
	ApplicationContext.class,
	SpELExpressionBeanParserTest.TestExpressionParserConfig.class,
	SpELExpressionBeanParserTest.TestBeanConfig.class
})
public class SpELExpressionBeanParserTest {

	@Autowired
	private CustomExpressionParser expressionParser;

	@Test
	@DisplayName("SpEL 표현식 파싱 테스트")
	public void parse_test() {
		// given
		String spEL = "@testBean.getValue()";

		// when
		String value = (String)expressionParser.parse(spEL);

		// then
		assertThat(value).isEqualTo("test");

	}

	@TestConfiguration
	public static class TestExpressionParserConfig{

		@Autowired
		private ApplicationContext applicationContext;

		@Bean
		public CustomExpressionParser expressionBeanParser(){
			StandardEvaluationContext context = new StandardEvaluationContext();
			context.setBeanResolver(this.beanResolver());
			return new SpELExpressionBeanParser(this.expressionParser(), context);
		}

		private ExpressionParser expressionParser(){
			return new SpelExpressionParser();
		}

		private BeanResolver beanResolver(){
			return (context, beanName) -> applicationContext.getBean(beanName);
		}

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