package com.gabia.weat.gcellapiserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.dto.FileDto;

public interface ExcelInfoRepositoryCustom {

	Page<ExcelInfo> findByMemberPaging(Member member, FileDto.FileListRequestDto fileListRequestDto
		, Pageable pageable);
}
