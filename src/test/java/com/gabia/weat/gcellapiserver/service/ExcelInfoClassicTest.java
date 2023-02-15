package com.gabia.weat.gcellapiserver.service;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
public class ExcelInfoClassicTest {
	@Autowired
	private ExcelInfoRepository excelInfoRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ExcelInfoService excelInfoService;
	@MockBean
	private MinioService minioService;

	private Member member;

	@BeforeEach
	public void setUp() {

		member = Member.builder()
			.memberId(1L)
			.email("test@gabia.com")
			.name("testName")
			.build();
	}

	@Test
	@DisplayName("엑셀 파일 삭제 테스트")
	public void deleteExcelFileTest() {
		// given
		ExcelInfo excelInfo = ExcelInfo.builder()
			.excelInfoId(1L)
			.name("testName")
			.path("testUrl")
			.isDeleted(false)
			.member(member)
			.build();

		memberRepository.save(member);
		excelInfoRepository.save(excelInfo);

		doNothing().when(minioService).deleteExcel(excelInfo);

		// when
		ExcelInfo deletedExcelInfo = excelInfoService.deleteExcelInfo(excelInfo.getMember().getEmail(), excelInfo.getExcelInfoId());

		// then
		assertThat(deletedExcelInfo.getIsDeleted()).isEqualTo(true);
	}


}
