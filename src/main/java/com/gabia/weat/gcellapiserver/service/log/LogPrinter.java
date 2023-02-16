package com.gabia.weat.gcellapiserver.service.log;

import com.gabia.weat.gcellapiserver.dto.log.LogFormatDto;

public interface LogPrinter {

	void print(LogFormatDto logFormatDto);

}