package com.gabia.weat.gcellapiserver.service;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MinioService {

	private final MinioClient minioClient;

	private final ExcelInfoRepository excelInfoRepository;

	@Value("${minio.bucket.name}")
	private String bucketName;

	public byte[] downloadExcel(Long excelInfoId) throws
		ServerException,
		InsufficientDataException,
		ErrorResponseException,
		IOException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		InvalidResponseException,
		XmlParserException,
		InternalException {

		ExcelInfo excelInfo = excelInfoRepository.findById(excelInfoId).orElseThrow(
			() -> new RuntimeException()
		);

		byte[] bytes = minioClient.getObject(
			GetObjectArgs.builder()
				.bucket(bucketName).object(excelInfo.getPath())
				.build()
		).readAllBytes();

		return bytes;

	}

}
