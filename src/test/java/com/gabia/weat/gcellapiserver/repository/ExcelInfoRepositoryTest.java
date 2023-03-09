package com.gabia.weat.gcellapiserver.repository;

import static com.gabia.weat.gcellapiserver.dto.FileDto.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.gabia.weat.gcellapiserver.config.QueryDslTestConfig;
import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.domain.type.ExcelStatusType;
import com.gabia.weat.gcellapiserver.dto.FileDto;
import com.gabia.weat.gcellapiserver.repository.enums.IdCondition;
import com.gabia.weat.gcellapiserver.repository.enums.NameCondition;

@DataJpaTest
@Import(QueryDslTestConfig.class)
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
			.status(ExcelStatusType.CREATED)
			.build();

		ExcelInfo saveExcelInfo = excelInfoRepository.save(excelInfo);
		String fileName = saveExcelInfo.getName();

		// when
		Optional<ExcelInfo> findExcelInfoOptional = excelInfoRepository.findByEmailAndName(member.getEmail(),
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
			.status(ExcelStatusType.DELETED)
			.build();

		ExcelInfo saveExcelInfo = excelInfoRepository.save(excelInfo);
		String fileName = saveExcelInfo.getName();

		// when
		Optional<ExcelInfo> findExcelInfoOptional = excelInfoRepository.findByEmailAndName(member.getEmail(),
			fileName);

		// then
		assertThat(findExcelInfoOptional.isPresent()).isFalse();
	}

	@Test
	@DisplayName("엑셀 조회 동적 정렬 테스트")
	public void getExcelInfoOrderTest(){
		// given
		ExcelInfo excelInfo1 = ExcelInfo.builder().name("abc").member(member).build();
		ExcelInfo excelInfo2 = ExcelInfo.builder().name("bcd").member(member).build();

		excelInfoRepository.save(excelInfo1);
		excelInfoRepository.save(excelInfo2);

		PageRequest namePaging = PageRequest.of(0,2, Sort.Direction.ASC, "name");
		PageRequest createdAtPaging =  PageRequest.of(0, 2, Sort.Direction.DESC, "createdAt");

		FileListRequestDto nameDto = FileListRequestDto.builder().build();
		FileListRequestDto createdAtDto = FileListRequestDto.builder().build();

		// when
		Page<FileListResponseDto> nameResult = excelInfoRepository.findByMemberPaging(member, namePaging, nameDto);
		Page<FileListResponseDto> createdAtResult = excelInfoRepository.findByMemberPaging(member, createdAtPaging, createdAtDto);

		// then
		assertThat(nameResult.getContent().get(0).fileName()).isEqualTo(excelInfo1.getName());
		assertThat(createdAtResult.getContent().get(0).fileName()).isEqualTo(excelInfo2.getName());
	}

	@Test
	@DisplayName("엑셀 조회 동적 조건 조회 테스트")
	public void getExcelInfoWhereTest(){
		// given
		int testSize = 10;
		LocalDateTime now = LocalDateTime.now();
		PageRequest pageRequest = PageRequest.of(0,testSize);

		for (int i = 0; i < testSize; i++) {
			ExcelStatusType status = i % 2 == 1? ExcelStatusType.DELETED : ExcelStatusType.CREATED;
			excelInfoRepository.save(ExcelInfo.builder().member(member).name("엔도" + Integer.toString(i))
				.status(status).build());
		}

		FileListRequestDto idInCondition = FileListRequestDto.builder().idCondition(IdCondition.NOT_IN)
			.excelInfoIdList(excelInfoRepository.findAll().stream()
				.map(e->e.getExcelInfoId())
				.filter(e -> (e % 2) == 1)
				.collect(Collectors.toList())).build();
		FileListRequestDto nameEqCondition = FileListRequestDto.builder().nameCondition(NameCondition.EQUAL)
			.fileName("엔도1").build();
		FileListRequestDto nameInCondition = FileListRequestDto.builder().nameCondition(NameCondition.LIKE)
			.fileName("엔도").build();
		FileListRequestDto createdAtGtCondition = FileListRequestDto.builder().minCreatedAt(now.minusMinutes(1)).build();
		FileListRequestDto createdAtLtCondition = FileListRequestDto.builder().maxCreatedAt(now.minusMinutes(1)).build();
		FileListRequestDto isDeleteCondition = FileListRequestDto.builder().status(ExcelStatusType.DELETED).build();
		FileListRequestDto nullCondition = FileListRequestDto.builder().build();

		// when
		Page<FileListResponseDto> idInResult = excelInfoRepository.findByMemberPaging(member, pageRequest, idInCondition);
		Page<FileListResponseDto> nameEqResult = excelInfoRepository.findByMemberPaging(member, pageRequest, nameEqCondition);
		Page<FileListResponseDto> nameInResult = excelInfoRepository.findByMemberPaging(member, pageRequest, nameInCondition);
		Page<FileListResponseDto> createdAtGtResult = excelInfoRepository.findByMemberPaging(member, pageRequest, createdAtGtCondition);
		Page<FileListResponseDto> createdAtLtResult = excelInfoRepository.findByMemberPaging(member, pageRequest, createdAtLtCondition);
		Page<FileListResponseDto> isDeleteResult = excelInfoRepository.findByMemberPaging(member, pageRequest, isDeleteCondition);
		Page<FileListResponseDto> nullResult = excelInfoRepository.findByMemberPaging(member, pageRequest, nullCondition);

		// then
		assertThat(idInResult.getContent().size()).isEqualTo(5);
		assertThat(nameEqResult.getContent().get(0).fileName()).isEqualTo(nameEqCondition.fileName());
		assertThat(nameInResult.getContent().size()).isEqualTo(testSize);
		assertThat(createdAtGtResult.getContent().size()).isEqualTo(testSize);
		assertThat(createdAtLtResult.getContent().size()).isEqualTo(0);
		assertThat(isDeleteResult.getContent().size()).isEqualTo(5);
		assertThat(nullResult.getContent().size()).isEqualTo(testSize);

	}

	@Test
	@DisplayName("엑셀 조회 복합 조건 정렬 테스트")
	public void getExcelInfoCompositeTest(){
		// given
		int testSize = 10;
		PageRequest namePaging = PageRequest.of(0,testSize, Sort.Direction.ASC, "name");
		PageRequest createdAtPaging = PageRequest.of(0, testSize, Sort.Direction.DESC, "createdAt");

		FileDto.FileListRequestDto nameInCondition = FileDto.FileListRequestDto.builder().nameCondition(NameCondition.LIKE)
			.fileName("엔도").build();

		for (int i = 0; i < testSize; i++) {
			ExcelStatusType status = i % 2 == 1? ExcelStatusType.DELETED : ExcelStatusType.CREATED;
			excelInfoRepository.save(ExcelInfo.builder().member(member).name("엔도" + Integer.toString(i))
				.status(status).build());
		}

		// when
		Page<FileListResponseDto> namePagingResult = excelInfoRepository.findByMemberPaging(member,  namePaging, nameInCondition);
		Page<FileListResponseDto> createdAtPagingResult = excelInfoRepository.findByMemberPaging(member, createdAtPaging, nameInCondition);

		// then
		assertThat(namePagingResult.getContent().get(0).fileName()).isEqualTo("엔도0");
		assertThat(createdAtPagingResult.getContent().get(0).fileName()).isEqualTo("엔도9");

	}


}
