package com.gabia.weat.gcellapiserver.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gabia.weat.gcellapiserver.dto.FileDTO.FileCreateRequestDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

import com.gabia.weat.gcellapiserver.dto.APIResponseDTO;
import com.gabia.weat.gcellapiserver.dto.FileDTO;
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
		FileCreateRequestDTO fileCreateRequestDTO = this.getFileCreateRequestDTO();

		given(excelInfoService.createExcel(any(), any())).willReturn(testExcelInfoId);

		// when
		APIResponseDTO response = excelInfoController.createExcel(fileCreateRequestDTO);

		// then
		assertThat(response.getResponse()).isEqualTo("/excels/" + testExcelInfoId);
	}

	private FileCreateRequestDTO getFileCreateRequestDTO() {
		return new FileDTO.FileCreateRequestDTO(
			"testName",
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