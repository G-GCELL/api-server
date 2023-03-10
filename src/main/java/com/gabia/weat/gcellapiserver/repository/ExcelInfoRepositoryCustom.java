package com.gabia.weat.gcellapiserver.repository;

import static com.gabia.weat.gcellapiserver.dto.FileDto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.dto.FileDto;

public interface ExcelInfoRepositoryCustom {

	Page<FileListResponseDto> findByMemberPaging(Member member, Pageable pageable,
		FileDto.FileListRequestDto fileListRequestDto
	);
}
