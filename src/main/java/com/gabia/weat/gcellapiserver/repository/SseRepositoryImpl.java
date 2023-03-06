package com.gabia.weat.gcellapiserver.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SseRepositoryImpl implements SseRepository {

	private ConcurrentHashMap<Long, List<SseEmitter>> sseMap = new ConcurrentHashMap<>();
	private ConcurrentHashMap<Long, SseEmitter> fileSseMap = new ConcurrentHashMap<>();

	@Override
	public SseEmitter save(Long id, SseEmitter sseEmitter) {
		List<SseEmitter> sseEmitters = sseMap.get(id);
		if (sseEmitters == null) {
			sseEmitters = new CopyOnWriteArrayList<>();
			sseMap.put(id, sseEmitters);
		}
		sseEmitters.add(sseEmitter);

		sseEmitter.onCompletion(() -> {
			log.info(id + "`s sseEmitter onCompletion");
			sseMap.get(id).remove(sseEmitter);
		});
		sseEmitter.onTimeout(() -> sseEmitter.complete());
		return sseEmitter;
	}

	@Override
	public Optional<SseEmitter> findById(Long id) {
		List<SseEmitter> sseEmitters = sseMap.get(id);
		int size = sseEmitters.size();
		return Optional.ofNullable(size == 0 ? null : sseEmitters.get(size - 1));
	}

	@Override
	public SseEmitter registryFileConnection(Long excelInfoId, SseEmitter sseEmitter) {
		fileSseMap.put(excelInfoId, sseEmitter);
		sseEmitter.onCompletion(() -> fileSseMap.remove(excelInfoId));
		sseEmitter.onTimeout(() -> sseEmitter.complete());
		return sseEmitter;
	}

	@Override
	public Optional<SseEmitter> findByExcelInfoId(Long excelInfoId) {
		return Optional.ofNullable(fileSseMap.get(excelInfoId));
	}

	@Override
	public List<SseEmitter> findListById(Long id) {
		return sseMap.getOrDefault(id, Collections.EMPTY_LIST);
	}

	@Override
	public void deleteByExcelInfoId(Long excelInfoId) {
		this.findByExcelInfoId(excelInfoId).ifPresent(sse -> sse.complete());
	}

}