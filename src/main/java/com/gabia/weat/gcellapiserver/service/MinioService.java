package com.gabia.weat.gcellapiserver.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

	private final MinioClient minioClient;
	private final ExcelInfoRepository excelInfoRepository;

	@Value("${minio.bucket.name}")
	private String bucketName;

	public byte[] downloadExcel(Long excelInfoId, String memberEmail) {

		ExcelInfo excelInfo = excelInfoRepository.findByIdAndMemberEmail(excelInfoId, memberEmail).orElseThrow(
			() -> new CustomException(ErrorCode.EXCEL_NOT_EXISTS)
		);
		try {
			byte[] bytes = minioClient.getObject(
				GetObjectArgs.builder()
					.bucket(bucketName).object(excelInfo.getPath())
					.build()
			).readAllBytes();
			return bytes;
		} catch (Exception e) {
			log.error("ExamInfo : " + excelInfo, e);
			throw new CustomException(ErrorCode.MINIO_ERROR);
		}
	}

}