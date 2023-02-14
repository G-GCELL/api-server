package com.gabia.weat.gcellapiserver.service;

import static com.gabia.weat.gcellapiserver.dto.FileDto.FileCreateRequestDto;
import static com.gabia.weat.gcellapiserver.dto.FileDto.FileUpdateNameRequestDto;
import static com.gabia.weat.gcellapiserver.dto.FileDto.FileUpdateNameResponseDto;
import static com.gabia.weat.gcellapiserver.dto.FileDto.FileListRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.weat.gcellapiserver.converter.FileDtoConverter;
import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.dto.FileDto;
import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;
import com.gabia.weat.gcellapiserver.service.producer.CreateRequestProducer;
import com.gabia.weat.gcellapiserver.util.ExcelInfoUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExcelInfoService {

	private final ExcelInfoUtil excelInfoUtil;
	private final MemberRepository memberRepository;
	private final ExcelInfoRepository excelInfoRepository;
	private final CreateRequestProducer createRequestProducer;
	private final MinioService minioService;

	@Transactional
	public Long createExcel(String email, FileCreateRequestDto fileCreateRequestDto) {
		Member member = this.getMemberByEmail(email);
		String randomFileName = excelInfoUtil.getRandomRealFileName();
		ExcelInfo excelInfo = excelInfoRepository.save(
			this.createNewExcelInfo(member, fileCreateRequestDto.fileName(), randomFileName)
		);
		this.sendExcelCreateRequestMessage(member.getMemberId(), randomFileName, fileCreateRequestDto);
		return excelInfo.getExcelInfoId();
	}

	@Transactional
	public FileUpdateNameResponseDto updateExcelInfoName(String memberEmail, Long excelInfoId,
		FileUpdateNameRequestDto fileUpdateNameRequestDto) {
		ExcelInfo excelInfo = excelInfoRepository.findByIdAndMemberEmail(excelInfoId, memberEmail).orElseThrow(
			() -> new CustomException(ErrorCode.EXCEL_NOT_EXISTS)
		);
		excelInfo.updateName(fileUpdateNameRequestDto.fileName());
		return FileDtoConverter.createEntityToUpdateNameResponseDto(excelInfo);
	}

	@Transactional
	public ExcelInfo deleteExcelInfo(String memberEmail, Long excelInfoId) {
		ExcelInfo excelInfo = excelInfoRepository.findByIdAndMemberEmail(excelInfoId, memberEmail)
			.orElseThrow(
				() -> new CustomException(ErrorCode.EXCEL_NOT_EXISTS)
			);
		excelInfoRepository.delete(excelInfo);
		minioService.deleteExcel(excelInfo);
		return excelInfo;
	}

	public Page<ExcelInfo> getExcelInfo(String memberEmail, Pageable pageable, FileListRequestDto fileListRequestDto){
		Member member = this.getMemberByEmail(memberEmail);
		return excelInfoRepository.findByMemberPaging(member, pageable, fileListRequestDto);
	}

	private Member getMemberByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(() -> {
			throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
		});
	}

	private ExcelInfo createNewExcelInfo(Member member, String fileName, String realFileName) {
		excelInfoRepository.findByMemberAndNameAndIsDeletedFalse(member, fileName).ifPresent(e -> {
			throw new CustomException(ErrorCode.DUPLICATE_FILE_NAME);
		});

		return ExcelInfo.builder()
			.member(member)
			.name(fileName)
			.path(realFileName)
			.isDeleted(false)
			.build();
	}

	private void sendExcelCreateRequestMessage(Long memberId, String newFileName,
		FileCreateRequestDto fileCreateRequestDto) {
		createRequestProducer.sendMessage(
			FileDtoConverter.createDtoToCreateMsgDto(memberId, newFileName, fileCreateRequestDto));
	}

}