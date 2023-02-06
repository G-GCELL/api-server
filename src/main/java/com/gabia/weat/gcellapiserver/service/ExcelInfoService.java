package com.gabia.weat.gcellapiserver.service;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import static com.gabia.weat.gcellapiserver.dto.FileDTO.FileCreateRequestDTO;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;
import com.gabia.weat.gcellapiserver.util.ExcelInfoUtil;

@Service
@RequiredArgsConstructor
public class ExcelInfoService {

	private final ExcelInfoUtil excelInfoUtil;
	private final MemberRepository memberRepository;
	private final ExcelInfoRepository excelInfoRepository;

	public Long createExcel(String email, FileCreateRequestDTO fileCreateRequestDTO) {
		Member member = this.getMemberByEmail(email);
		String path = excelInfoUtil.getRandomRealFileName();
		ExcelInfo excelInfo = excelInfoRepository.save(this.getExcelInfo(member, fileCreateRequestDTO.fileName(), path));
		// 메시지 전송

		return excelInfo.getExcelInfoId();
	}

	private Member getMemberByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(() -> {
			throw new EntityNotFoundException("");
		});
	}

	private ExcelInfo getExcelInfo(Member member, String fileName, String path) {
		excelInfoRepository.findByMemberAndName(member, fileName).ifPresent(e -> {
			throw new IllegalStateException("");
		});

		return ExcelInfo.builder()
			.member(member)
			.name(fileName)
			.path(path)
			.isDeleted(false)
			.build();
	}

}