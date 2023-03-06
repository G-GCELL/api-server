package com.gabia.weat.gcellapiserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseRepository {

	SseEmitter save(Long id, SseEmitter sseEmitter);

	Optional<SseEmitter> findById(Long id);

	SseEmitter registryFileConnection(Long excelInfoId, SseEmitter sseEmitter);

	Optional<SseEmitter> findByExcelInfoId(Long excelInfoId);

	List<SseEmitter> findListById(Long id);

	void deleteByExcelInfoId(Long excelInfoId);

}