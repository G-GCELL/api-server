package com.gabia.weat.gcellapiserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	MINIO_ERROR(500, "MinIO 라이브러리 에러"),
	EXCEL_NOT_EXISTS(404, "해당 엑셀파일이 존재하지 않습니다."),
	EXCEL_NOT_MATCHES(401, "액셀파일 유저와 해당 유저가 일치하지 않습니다."),
	MEMBER_NOT_FOUND(404, "해당 사용자가 존재하지 않습니다."),
	DUPLICATE_FILE_NAME(409, "해당 파일 이름이 이미 존재합니다.");
	private final int status;
	private final String message;

}