package com.gabia.weat.gcellapiserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomStatus {
	ESSENTIAL_VALUE_ERROR(94000),
	MEMBER_NOT_FOUND(14040),
	MINIO_ERROR(25000),
	EXCEL_NOT_EXISTS(24040),
	EXCEL_NOT_MATCHES(24030),
	EXCEL_DELETED(24050),
	DUPLICATE_FILE_NAME(24090),
	CONNECTION_NOT_FOUND(34040),
	CONNECTION_ERROR(35000),
	UNKNOWN_ERROR(95000);

	private int status;

}