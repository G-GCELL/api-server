package com.gabia.weat.gcellapiserver.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.domain.type.ExcelStatusType;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;

import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

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
		member = Member.builder().memberId(1L).name("안태욱").email("test@test.com").build();
		excelInfo = ExcelInfo.builder().excelInfoId(1L).path("미니오에 저장될 파일 이름")
			.name("데이터베이스에 저장될 파일 이름").status(ExcelStatusType.CREATED).member(member)
			.build();
	}

	@Test()
	@DisplayName("액셀 다운로드 테스트")
	public void excelDownloadTest() throws Exception {
		// given
		byte[] bytes = {0, 1, 2};
		String testEmail = "test@test.com";
		InputStream inputStream = new ByteArrayInputStream(bytes);
		inputStream.close();
		GetObjectResponse getObjectResponse = new GetObjectResponse(null, null, null, null, inputStream);
		ReflectionTestUtils.setField(minioService, "excelBucketName", "test");

		// mocking
		given(excelInfoRepository.findByIdAndMemberEmail(any(), any())).willReturn(Optional.ofNullable(excelInfo));
		given(minioClient.getObject(any())).willReturn(getObjectResponse);

		// when
		byte[] resultBytes = minioService.downloadExcel(excelInfo.getExcelInfoId(), testEmail);

		// then
		Assertions.assertThat(resultBytes).isEqualTo(bytes);
	}

	@Test
	@DisplayName("minio client 에러 테스트")
	public void minioClientErrorTest() throws Exception {
		// given
		int testLength = 9;
		String testEmail = "test@test.com";
		ReflectionTestUtils.setField(minioService, "excelBucketName", "test");

		// mocking
		given(excelInfoRepository.findByIdAndMemberEmail(any(), any())).willReturn(Optional.ofNullable(excelInfo));

		// when
		when(minioClient.getObject(any()))
			.thenThrow(IOException.class)
			.thenThrow(NoSuchAlgorithmException.class)
			.thenThrow(InvalidKeyException.class)
			.thenThrow(InvalidResponseException.class)
			.thenThrow(InternalException.class)
			.thenThrow(ServerException.class)
			.thenThrow(InsufficientDataException.class)
			.thenThrow(mock(XmlParserException.class))
			.thenThrow(mock(ErrorResponseException.class));

		// then
		for (int i = 0; i < testLength; i++) {
			org.junit.jupiter.api.Assertions.assertThrows(CustomException.class, () -> {
				minioService.downloadExcel(excelInfo.getExcelInfoId(), testEmail);
			});
		}

	}

	@Test
	@DisplayName("파일_업로드_테스트")
	public void upload_test() throws Exception {
		// given
		MultipartFile mockFile = mock(MultipartFile.class);
		InputStream mockInputStream = mock(InputStream.class);

		ReflectionTestUtils.setField(minioService, "csvBucketName", "test");

		given(mockFile.getInputStream()).willReturn(mockInputStream);
		given(mockFile.getSize()).willReturn(1L);

		// when
		minioService.upload(mockFile, "mock");

		// then
		verify(minioClient, times(1)).putObject(any());

	}

}