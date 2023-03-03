package com.gabia.weat.gcellapiserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExcelInfoRepository extends JpaRepository<ExcelInfo, Long>, ExcelInfoRepositoryCustom {

	Optional<ExcelInfo> findByMemberAndNameAndIsDeletedFalse(Member member, String name);

	@Query("select e from excel_info e join fetch e.member m where e.excelInfoId =:excelInfoId and e.isDeleted = false and m.email =:email")
	Optional<ExcelInfo> findByIdAndMemberEmail(@Param("excelInfoId") Long excelInfoId, @Param("email") String email);

}