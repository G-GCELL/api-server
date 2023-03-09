package com.gabia.weat.gcellapiserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("prod")
@Component
@RequiredArgsConstructor
public class ApplicationInit {

	private final MinioClient minioClient;

	@Value("${minio.bucket.excel}")
	private String excelBucketName;
	@Value("${minio.bucket.csv}")
	private String csvBucketName;

	@PostConstruct
	public void initMinio(){
		this.createBucket(excelBucketName);
		this.createBucket(csvBucketName);
	}

	private void createBucket(String bucketName) {
		try {
			boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
			if (!bucketExists){
				minioClient.makeBucket(
					MakeBucketArgs.builder()
						.bucket(bucketName)
						.build());
			}
		}catch (Exception e){
			log.error(" Minio Init Error " , e);
			throw new CustomException(ErrorCode.MINIO_ERROR);
		}
	}

}
