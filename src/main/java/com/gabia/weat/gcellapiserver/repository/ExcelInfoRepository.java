package com.gabia.weat.gcellapiserver.repository;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelInfoRepository extends JpaRepository<ExcelInfo, Long> {
}
