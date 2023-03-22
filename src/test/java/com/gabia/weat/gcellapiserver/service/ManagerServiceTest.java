package com.gabia.weat.gcellapiserver.service;

import static com.gabia.weat.gcellapiserver.dto.FileDto.*;
import static org.mockito.BDDMockito.*;

import java.time.YearMonth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.weat.gcellapiserver.service.producer.CsvUpdateRequestProducer;
import com.gabia.weat.gcellcommonmodule.dto.format.LogFormatFactory;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceTest {

	@Mock
	private MinioService minioService;
	@Mock
	private CsvUpdateRequestProducer csvUpdateRequestProducer;
	@Mock
	private LogFormatFactory logFormatFactory;
	@InjectMocks
	private AdminService adminService;

	@Test
	@DisplayName("CSV_데이터셋_수정_요청_테스트")
	public void updateCsvFile_test() {
		// given
		MultipartFile mockFile = mock(MultipartFile.class);
		String testEmail = "test@gabia.com";
		CsvUpdateRequestDto csvUpdateRequestDto = this.getCsvUpdateRequestDto(mockFile);

		given(mockFile.getOriginalFilename()).willReturn("test.csv");
		given(logFormatFactory.getTraceId()).willReturn("testTraceId");

		// when
		adminService.updateCsvFile(testEmail, csvUpdateRequestDto);

		// then
		verify(minioService, times(1)).upload(any(), any());
		verify(csvUpdateRequestProducer, times(1)).sendMessage(any());
		verify(logFormatFactory, times(1)).getTraceId();
	}

	private CsvUpdateRequestDto getCsvUpdateRequestDto(MultipartFile mockFile) {
		return new CsvUpdateRequestDto(
			mockFile,
			YearMonth.now()
		);
	}

}