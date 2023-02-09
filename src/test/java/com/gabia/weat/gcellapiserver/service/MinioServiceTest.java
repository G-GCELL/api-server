package com.gabia.weat.gcellapiserver.service;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoExtension.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MinioServiceTest {
	@InjectMocks
	private MinioService minioService;
	@Mock
	private ExcelInfoRepository excelInfoRepository;
	@Mock
	private MinioClient minioClient;
	private Member member;
	private ExcelInfo excelInfo;

	@BeforeEach
	public void setUp() {
		member = Member.builder().memberId(1L).name("안태욱").email("test@test.com")
			.password("password").build();
		excelInfo = ExcelInfo.builder().excelInfoId(1L).path("미니오에 저장될 파일 이름")
			.name("데이터베이스에 저장될 파일 이름").isDeleted(false).member(member)
			.build();
	}

	@Test()
	@DisplayName("액셀 다운로드 테스트")
	public void excelDownloadTest() throws
		ServerException,
		InsufficientDataException,
		ErrorResponseException,
		IOException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		InvalidResponseException,
		XmlParserException,
		InternalException {

		// given
		byte[] bytes = {0, 1, 2};
		String testEmail = "test@test.com";
		InputStream inputStream = new ByteArrayInputStream(bytes);
		inputStream.close();
		GetObjectResponse getObjectResponse = new GetObjectResponse(null, null, null, null, inputStream);
		ReflectionTestUtils.setField(minioService, "bucketName", "test");

		// mocking
		given(excelInfoRepository.findByIdFetchJoin(any())).willReturn(Optional.ofNullable(excelInfo));
		given(minioClient.getObject(any())).willReturn(getObjectResponse);

		// when
		byte[] resultBytes = minioService.downloadExcel(excelInfo.getExcelInfoId(), testEmail);

		// then
		Assertions.assertThat(resultBytes).isEqualTo(bytes);
	}
}
