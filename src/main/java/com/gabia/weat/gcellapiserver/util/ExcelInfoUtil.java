package com.gabia.weat.gcellapiserver.util;

import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "excel-info")
public class ExcelInfoUtil {

	private String fileBaseUrl;
	private String excelExtension;

	public String getRandomRealFileName(){
		return new StringBuffer(fileBaseUrl).append(UUID.randomUUID()).append(excelExtension).toString();
	}

}