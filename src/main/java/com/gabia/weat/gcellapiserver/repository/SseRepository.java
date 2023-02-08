package com.gabia.weat.gcellapiserver.repository;

import java.util.Optional;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SseRepository {

	SseEmitter save(Long id, SseEmitter sseEmitter);

	Optional<SseEmitter> findById(Long id);

}