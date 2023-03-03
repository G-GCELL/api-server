package com.gabia.weat.gcellapiserver.parser;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpELExpressionBeanParser implements CustomExpressionParser {

	private final ExpressionParser parser;
	private final StandardEvaluationContext context;

	@Override
	public Object parse(String value) {
		Expression expression = parser.parseExpression(value);
		return expression.getValue(context);
	}

}