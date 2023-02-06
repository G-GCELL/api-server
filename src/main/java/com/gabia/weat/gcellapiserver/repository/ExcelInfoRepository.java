package com.gabia.weat.gcellapiserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.Member;

public interface ExcelInfoRepository extends JpaRepository<ExcelInfo, Long> {

	Optional<ExcelInfo> findByMemberAndName(Member member, String name);

}