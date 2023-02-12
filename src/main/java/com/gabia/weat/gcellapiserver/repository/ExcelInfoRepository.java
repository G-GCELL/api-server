package com.gabia.weat.gcellapiserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;

import org.springframework.data.jpa.repository.Query;

public interface ExcelInfoRepository extends JpaRepository<ExcelInfo, Long>, ExcelInfoRepositoryCustom {

	Optional<ExcelInfo> findByMemberAndName(Member member, String name);

	@Query("select e from excel_info e join fetch e.member where e.excelInfoId =:excelInfoId")
	Optional<ExcelInfo> findByIdFetchJoin(Long excelInfoId);

}