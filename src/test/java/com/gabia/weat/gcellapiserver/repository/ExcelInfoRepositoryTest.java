package com.gabia.weat.gcellapiserver.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;

@DataJpaTest
public class ExcelInfoRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private ExcelInfoRepository excelInfoRepository;
	private ExcelInfo excelInfo;
	private Member member;

	@BeforeEach
	public void setUp() {
		member = Member.builder()
			.name("test")
			.email("test@gabia.com")
			.password("testPassword")
			.build();

		member = memberRepository.save(member);
	}

	@Test
	@DisplayName("사용자와 파일 이름으로 엑셀 데이터 조회 삭제되지 않았을 경우 테스트")
	public void findByMemberAndNameAndIsDeletedFalse_isDeleted_false_test() {
		// given
		excelInfo = ExcelInfo.builder()
			.member(member)
			.name("testName")
			.path("testPath")
			.isDeleted(false)
			.build();

		ExcelInfo saveExcelInfo = excelInfoRepository.save(excelInfo);
		String fileName = saveExcelInfo.getName();

		// when
		Optional<ExcelInfo> findExcelInfoOptional = excelInfoRepository.findByMemberAndNameAndIsDeletedFalse(member,
			fileName);

		// then
		assertThat(findExcelInfoOptional.isPresent()).isTrue();
	}

	@Test
	@DisplayName("사용자와 파일 이름으로 엑셀 데이터 조회 삭제되었을 경우 테스트")
	public void findByMemberAndNameAndIsDeletedFalse_isDeleted_true_test() {
		// given
		excelInfo = ExcelInfo.builder()
			.member(member)
			.name("testName")
			.path("testPath")
			.isDeleted(true)
			.build();

		ExcelInfo saveExcelInfo = excelInfoRepository.save(excelInfo);
		String fileName = saveExcelInfo.getName();

		// when
		Optional<ExcelInfo> findExcelInfoOptional = excelInfoRepository.findByMemberAndNameAndIsDeletedFalse(member,
			fileName);

		// then
		assertThat(findExcelInfoOptional.isPresent()).isFalse();
	}

}