package com.gabia.weat.gcellapiserver.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	BAD_REQUEST(HttpStatus.BAD_REQUEST, CustomStatus.ESSENTIAL_VALUE_ERROR, "요청 값이 잘못되었습니다."),
	MINIO_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, CustomStatus.MINIO_ERROR, "MinIO 라이브러리 에러"),
	EXCEL_NOT_EXISTS(HttpStatus.NOT_FOUND, CustomStatus.EXCEL_NOT_EXISTS, "해당 엑셀파일이 존재하지 않습니다."),
	EXCEL_NOT_MATCHES(HttpStatus.FORBIDDEN, CustomStatus.EXCEL_NOT_MATCHES, "액셀파일 유저와 해당 유저가 일치하지 않습니다."),
	EXCEL_DELETED(HttpStatus.BAD_REQUEST, CustomStatus.EXCEL_DELETED, "삭제된 엑셀파일입니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, CustomStatus.MEMBER_NOT_FOUND, "해당 사용자가 존재하지 않습니다."),
	DUPLICATE_FILE_NAME(HttpStatus.CONFLICT, CustomStatus.DUPLICATE_FILE_NAME, "해당 파일 이름이 이미 존재합니다."),
	CONNECTION_NOT_FOUND(HttpStatus.NOT_FOUND, CustomStatus.CONNECTION_NOT_FOUND, "해당 사용자의 연결을 찾을 수 없습니다."),
	CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, CustomStatus.CONNECTION_ERROR, "사용자 연결에 문제가 발생하였습니다.");
	private final HttpStatus status;
	private final CustomStatus code;
	private final String message;

}