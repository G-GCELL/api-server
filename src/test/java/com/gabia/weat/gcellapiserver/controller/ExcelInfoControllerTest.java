package com.gabia.weat.gcellapiserver.controller;

import static com.gabia.weat.gcellapiserver.dto.FileDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.weat.gcellapiserver.dto.ApiResponseDto;
import com.gabia.weat.gcellapiserver.service.ExcelInfoService;

@ExtendWith(MockitoExtension.class)
public class ExcelInfoControllerTest {

	@Mock
	private ExcelInfoService excelInfoService;
	@InjectMocks
	private ExcelInfoController excelInfoController;

	@Test
	@DisplayName("엑셀_생성_API_테스트")
	public void createExcel_test() {
		// given
		Long testExcelInfoId = 1L;
		FileCreateRequestDto fileCreateRequestDTO = this.getFileCreateRequestDTO();

		given(excelInfoService.createExcel(any(), any())).willReturn(testExcelInfoId);

		// when
		ApiResponseDto response = excelInfoController.createExcel(fileCreateRequestDTO).getBody();

		// then
		assertThat(response.getResponse()).isEqualTo("/excels/" + testExcelInfoId);
	}

	private FileCreateRequestDto getFileCreateRequestDTO() {
		return new FileCreateRequestDto(
			"testName",
			List.of("test_column"),
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null);
	}

}
