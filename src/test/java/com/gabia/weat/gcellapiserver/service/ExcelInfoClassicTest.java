package com.gabia.weat.gcellapiserver.service;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.dto.FileDto;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;
import com.gabia.weat.gcellapiserver.repository.enums.CreatedAtCondition;
import com.gabia.weat.gcellapiserver.repository.enums.NameCondition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static com.gabia.weat.gcellapiserver.dto.FileDto.FileListRequestDto;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

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
