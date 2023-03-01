package com.gabia.weat.gcellapiserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExcelInfoRepository extends JpaRepository<ExcelInfo, Long>, ExcelInfoRepositoryCustom {

	@Query("select e from excel_info e join fetch e.member m where e.member = :member and e.name = :name and e.status = 'CREATED'")
	Optional<ExcelInfo> findByMemberAndNameAndStatusCreated(Member member, String name);

	@Query("select e from excel_info e join fetch e.member m where e.excelInfoId =:excelInfoId and e.status = 'CREATED' and m.email =:email")
	Optional<ExcelInfo> findByIdAndMemberEmail(@Param("excelInfoId") Long excelInfoId, @Param("email") String email);

}