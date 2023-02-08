package com.gabia.weat.gcellapiserver.repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class SseRepositoryImpl implements SseRepository {

	private ConcurrentHashMap<Long, SseEmitter> sseMap = new ConcurrentHashMap<>();

	@Override
	public SseEmitter save(Long id, SseEmitter sseEmitter) {
		sseMap.put(id, sseEmitter);
		sseEmitter.onCompletion(() -> {
			log.info(id + "`s sseEmitter onCompletion");
			sseMap.remove(id);
		});
		sseEmitter.onTimeout(() -> {
			log.info(id + "`s sseEmitter onTimeout");
			sseEmitter.complete();
		});
		return sseEmitter;
	}

	@Override
	public Optional<SseEmitter> findById(Long id) {
		return Optional.ofNullable(sseMap.get(id));
	}

}