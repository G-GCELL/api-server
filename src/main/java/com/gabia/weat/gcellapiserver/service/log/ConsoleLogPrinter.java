package com.gabia.weat.gcellapiserver.service.log;

import org.springframework.stereotype.Component;

import com.gabia.weat.gcellapiserver.dto.log.LogFormatDto;
import com.gabia.weat.gcellapiserver.dto.log.LogFormatFactory;
import com.gabia.weat.gcellapiserver.parser.CustomExpressionParser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsoleLogPrinter extends LogPrinter {

	public ConsoleLogPrinter(LogFormatFactory logFormatFactory,
		CustomExpressionParser expressionBeanParser) {
		super(logFormatFactory, expressionBeanParser);
	}

	@Override
	public void print(LogFormatDto logFormatDto) {
		log.atLevel(logFormatDto.getLevel()).log(logFormatDto.toString());
	}

}