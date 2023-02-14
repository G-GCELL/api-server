package com.gabia.weat.gcellapiserver.service;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

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
	private ExcelInfo excelInfo;

	@BeforeEach
	public void setUp() {
		member = Member.builder()
			.memberId(1L)
			.email("test@gabia.com")
			.name("testName")
			.build();

		excelInfo = ExcelInfo.builder()
			.excelInfoId(1L)
			.name("testName")
			.path("testUrl")
			.isDeleted(false)
			.member(member)
			.build();
	}

	@Test
	@DisplayName("엑셀 파일 삭제 테스트")
	public void deleteExcelFileTest() {
		// given
		memberRepository.save(member);
		excelInfoRepository.save(excelInfo);
		doNothing().when(minioService).deleteExcel(excelInfo);

		// when
		excelInfo = excelInfoService.deleteExcelInfo(excelInfo.getMember().getEmail(), excelInfo.getExcelInfoId());

		// then
		assertThat(excelInfo.getIsDeleted()).isEqualTo(true);
	}

}
