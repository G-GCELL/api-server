package com.gabia.weat.gcellapiserver.repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class SseRepositoryImpl implements SseRepository{

	private ConcurrentHashMap<Long, SseEmitter> sseMap;

	public SseRepositoryImpl() {
		this.sseMap = new ConcurrentHashMap<>();
	}

	@Override
	public SseEmitter save(Long id, SseEmitter sseEmitter) {
		return null;
	}

	@Override
	public Optional<SseEmitter> findById(Long id) {
		return Optional.empty();
	}
}