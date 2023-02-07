package com.gabia.weat.gcellapiserver.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;

public class SseRepositoryImplTest {

	private SseRepository sseRepository = new SseRepositoryImpl();

	@Test
	@DisplayName("SSE_조회_테스트")
	public void findById_test() {
		// given
		Long id = 1L;
		SseEmitter testSse = new SseEmitter();
		sseRepository.save(id, testSse);

		// when
		boolean isPresent = sseRepository.findById(id).isPresent();

		// then
		assertThat(isPresent).isTrue();
	}

	@Test
	@DisplayName("SSE_저장_테스트")
	public void save_test() {
		// given
		Long id = 1L;
		SseEmitter testSse = new SseEmitter();

		// when
		sseRepository.save(id, testSse);

		// then
		assertThat(sseRepository.findById(id).isPresent()).isTrue();
	}

}