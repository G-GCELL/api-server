package com.gabia.weat.gcellapiserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomStatus {

	MEMBER_NOT_FOUND(14040),
	MINIO_ERROR(25000),
	EXCEL_NOT_EXISTS(24040),
	EXCEL_NOT_MATCHES(24030),
	DUPLICATE_FILE_NAME(24090),
	CONNECTION_NOT_FOUND(34040),
	CONNECTION_ERROR(35000);

	private int status;

}