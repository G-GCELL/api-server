package com.gabia.weat.gcellapiserver.service.log;

import org.springframework.stereotype.Component;

import com.gabia.weat.gcellapiserver.dto.log.LogFormatDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConsoleLogPrinter implements LogPrinter {

	@Override
	public void print(LogFormatDto logFormatDto) {
		log.atLevel(logFormatDto.getLevel()).log(logFormatDto.toString());
	}

}