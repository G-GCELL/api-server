package com.gabia.weat.gcellapiserver.dto.log;

import org.slf4j.event.Level;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogFormatDto {

	protected Level level;
	protected String serverName;
	protected String traceId;

}