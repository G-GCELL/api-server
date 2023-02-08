package com.gabia.weat.gcellapiserver.service;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;

import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MinioService {

	private final MinioClient minioClient;
	private final ExcelInfoRepository excelInfoRepository;

	@Value("${minio.bucket.name}")
	private String bucketName;

	public byte[] downloadExcel(Long excelInfoId, String memberEmail) {

		ExcelInfo excelInfo = excelInfoRepository.findById(excelInfoId).orElseThrow(
			() -> new CustomException(ErrorCode.EXCEL_NOT_EXISTS)
		);
		if (!excelInfo.getMember().getEmail().equals(memberEmail)) {
			throw new CustomException(ErrorCode.EXCEL_NOT_MATCHES);
		}
		try {
			byte[] bytes = minioClient.getObject(
				GetObjectArgs.builder()
					.bucket(bucketName).object(excelInfo.getPath())
					.build()
			).readAllBytes();
			return bytes;
		} catch (Exception e) {
			throw new CustomException(ErrorCode.MINIO_ERROR);
		}

	}

}
