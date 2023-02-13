package com.gabia.weat.gcellapiserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;

import org.springframework.data.jpa.repository.Query;

public interface ExcelInfoRepository extends JpaRepository<ExcelInfo, Long> {

	Optional<ExcelInfo> findByMemberAndName(Member member, String name);

	@Query("select e from excel_info e join fetch e.member m where e.excelInfoId =:excelInfoId and e.isDeleted = false and m.email =:email")
	Optional<ExcelInfo> findByIdAndMemberEmail(Long excelInfoId, String email);

}