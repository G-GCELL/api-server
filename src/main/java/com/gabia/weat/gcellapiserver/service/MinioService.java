package com.gabia.weat.gcellapiserver.service;

import java.io.InputStream;

import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
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

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

	private final MinioClient minioClient;
	private final ExcelInfoRepository excelInfoRepository;

	@Value("${minio.bucket.excel}")
	private String excelBucketName;
	@Value("${minio.bucket.csv}")
	private String csvBucketName;

	public byte[] downloadExcel(Long excelInfoId, String memberEmail) {

		ExcelInfo excelInfo = excelInfoRepository.findByIdAndMemberEmail(excelInfoId, memberEmail).orElseThrow(
			() -> new CustomException(ErrorCode.EXCEL_NOT_EXISTS)
		);
		try {
			byte[] bytes = minioClient.getObject(
				GetObjectArgs.builder()
					.bucket(excelBucketName).object(excelInfo.getPath())
					.build()
			).readAllBytes();
			return bytes;
		} catch (Exception e) {
			log.error("ExamInfo : " + excelInfo, e);
			throw new CustomException(ErrorCode.MINIO_ERROR);
		}
	}

	public void deleteExcel(ExcelInfo excelInfo) {
		try {
			minioClient.removeObject(
				RemoveObjectArgs.builder()
					.bucket(excelBucketName)
					.object(excelInfo.getPath())
					.build()
			);
		} catch (Exception e) {
			log.error("ExamInfo : " + excelInfo, e);
			throw new CustomException(ErrorCode.MINIO_ERROR);
		}
	}

	public void upload(MultipartFile multipartFile) {
		try (InputStream inputStream = multipartFile.getInputStream()) {
			minioClient.putObject(PutObjectArgs.builder()
				.bucket(csvBucketName)
				.object(multipartFile.getOriginalFilename())
				.stream(inputStream, multipartFile.getSize(), -1)
				.build()
			);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.MINIO_ERROR);
		}
	}

}