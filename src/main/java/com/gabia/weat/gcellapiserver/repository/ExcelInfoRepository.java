package com.gabia.weat.gcellapiserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExcelInfoRepository extends JpaRepository<ExcelInfo, Long>, ExcelInfoRepositoryCustom {

	@Query("select e from excel_info e join fetch e.member m where e.member.email = :email and e.name = :name and e.status != 'DELETED'")
	Optional<ExcelInfo> findByEmailAndName(String email, String name);

	@Query("select e from excel_info e join fetch e.member m where e.excelInfoId =:excelInfoId and e.status != 'DELETED' and m.email =:email")
	Optional<ExcelInfo> findByIdAndMemberEmail(@Param("excelInfoId") Long excelInfoId, @Param("email") String email);

}