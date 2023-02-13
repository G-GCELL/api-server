package com.gabia.weat.gcellapiserver.service;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.gabia.weat.gcellapiserver.dto.FileDto.FileCreateRequestDto;
import static com.gabia.weat.gcellapiserver.dto.FileDto.FileUpdateNameRequestDto;
import static com.gabia.weat.gcellapiserver.dto.FileDto.FileUpdateNameResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;
import com.gabia.weat.gcellapiserver.service.producer.CreateRequestProducer;
import com.gabia.weat.gcellapiserver.util.ExcelInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;
@ExtendWith(MockitoExtension.class)
public class ExcelInfoServiceTest {

	@Mock
	private ExcelInfoUtil excelInfoUtil;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private ExcelInfoRepository excelInfoRepository;
	@Mock
	private CreateRequestProducer createRequestProducer;
	@InjectMocks
	private ExcelInfoService excelInfoService;

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
	@DisplayName("엑셀_생성_성공_테스트")
	public void createExcel_success_test() {
		// given
		String testEmail = getTestEmail();
		FileCreateRequestDto fileCreateRequestDTO = this.getFileCreateRequestDTO();

		given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
		given(excelInfoRepository.findByMemberAndNameAndIsDeletedFalse(any(), any())).willReturn(Optional.empty());
		given(excelInfoUtil.getRandomRealFileName()).willReturn("testFileName.xlsx");
		given(excelInfoRepository.save(any())).willReturn(excelInfo);

		// when
		Long saveExcelInfoId = excelInfoService.createExcel(testEmail, fileCreateRequestDTO);

		// then
		assertThat(saveExcelInfoId).isEqualTo(excelInfo.getExcelInfoId());
		verify(createRequestProducer, times(1)).sendMessage(any());
	}

	@Test
	@DisplayName("엑셀_생성_실패_테스트_사용자_미조회")
	public void createExcel_not_found_member_test() {
		// given
		String testEmail = getTestEmail();
		FileCreateRequestDto fileCreateRequestDTO = this.getFileCreateRequestDTO();

		given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

		// when & then
		assertThrows(CustomException.class,
			() -> excelInfoService.createExcel(testEmail, fileCreateRequestDTO));
	}

	@Test
	@DisplayName("엑셀_생성_실패_테스트_파일_이름_중복")
	public void createExcel_duplicate_filename_test() {
		// given
		String testEmail = getTestEmail();
		FileCreateRequestDto fileCreateRequestDTO = this.getFileCreateRequestDTO();

		given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
		given(excelInfoUtil.getRandomRealFileName()).willReturn("testPath");
		given(excelInfoRepository.findByMemberAndNameAndIsDeletedFalse(any(), any())).willReturn(Optional.of(excelInfo));

		// when & then
		assertThrows(CustomException.class, () -> excelInfoService.createExcel(testEmail, fileCreateRequestDTO));
	}

	@Test
	@DisplayName("엑셀 파일이름 변경 테스트")
	public void updateExcelFileNameTest(){
		// given
		Long excelInfoId = 1L;
		String memberEmail =  getTestEmail();
		FileUpdateNameRequestDto fileUpdateNameRequestDto = this.getFileUpdateNameRequestDto();

		given(excelInfoRepository.findByIdAndMemberEmail(any(), any())).willReturn(Optional.of(excelInfo));

		// when
		FileUpdateNameResponseDto fileUpdateNameResponseDto = excelInfoService.updateExcelInfoName(memberEmail, excelInfoId, fileUpdateNameRequestDto);

		// then
		assertThat(fileUpdateNameResponseDto.fileName()).isEqualTo(fileUpdateNameRequestDto.fileName());
	}

	private FileCreateRequestDto getFileCreateRequestDTO() {
		return new FileCreateRequestDto(
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

	private String getTestEmail(){
		return "test@gabia.com";
	}

	private FileUpdateNameRequestDto getFileUpdateNameRequestDto(){
		return FileUpdateNameRequestDto.builder()
			.fileName("변경할 이름")
			.build();
	}

}