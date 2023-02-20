package com.gabia.weat.gcellapiserver.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.gabia.weat.gcellapiserver.parser.CustomExpressionBeanParser;
import com.gabia.weat.gcellapiserver.parser.SpELExpressionBeanParser;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ExpressionBeanParserConfig {

	private final ApplicationContext applicationContext;

	@Bean
	public CustomExpressionBeanParser expressionBeanParser(){
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