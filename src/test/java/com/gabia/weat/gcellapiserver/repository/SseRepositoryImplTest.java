package com.gabia.weat.gcellapiserver.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

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
		Optional<SseEmitter> emitterOptional = sseRepository.findById(id);

		// then
		assertThat(emitterOptional.isPresent()).isTrue();
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
		Optional<SseEmitter> emitterOptional = sseRepository.findById(id);
		assertThat(emitterOptional.isPresent()).isTrue();
	}

	@Test
	@DisplayName("SSE_파일_번호_연동_테스트")
	public void registryFileConnection() {
		Long id = 1L;
		SseEmitter testSse = new SseEmitter();

		// when
		sseRepository.registryFileConnection(id, testSse);

		// then
		Optional<SseEmitter> emitterOptional = sseRepository.findByExcelInfoId(id);
		assertThat(emitterOptional.isPresent()).isTrue();
	}

	@Test
	@DisplayName("SSE_파일_id로_조회_테스트")
	public void findByExcelInfoId_test() {
		// given
		Long id = 1L;
		SseEmitter testSse = new SseEmitter();
		sseRepository.registryFileConnection(id, testSse);

		// when
		Optional<SseEmitter> emitterOptional = sseRepository.findByExcelInfoId(id);

		// then
		assertThat(emitterOptional.isPresent()).isTrue();
	}

	@Test
	@DisplayName("SSE_정보_리스트_조회")
	public void findListById(){
		// given
		Long id = 1L;
		SseEmitter testSse = new SseEmitter();
		sseRepository.save(id, testSse);

		// when
		List<SseEmitter> list = sseRepository.findListById(id);

		// then
		assertThat(list).hasSize(1);

	}

}