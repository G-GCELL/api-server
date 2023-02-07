package com.gabia.weat.gcellapiserver.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.domain.type.MessageType;
import com.gabia.weat.gcellapiserver.error.exception.SseNotConnectException;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;
import com.gabia.weat.gcellapiserver.repository.SseRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseService {

	private final SseRepository sseRepository;
	private final MemberRepository memberRepository;

	public Long connect(String email, SseEmitter sseEmitter) {
		Member member = this.getMemberByEmail(email);
		Long memberId = member.getMemberId();
		sseRepository.save(memberId, sseEmitter);
		messaging("connect", MessageType.CONNECT, sseEmitter);
		return memberId;
	}

	public void sendMessage(Long memberId, MessageType messageType, Object message) {
		sseRepository.findById(memberId).ifPresent(sse -> messaging(message, messageType, sse));
	}

	private void messaging(Object message, MessageType messageType, SseEmitter sseEmitter) {
		try {
			sseEmitter.send(SseEmitter.event()
				.name(messageType.name())
				.data(message));
		} catch (IOException e) {
			throw new SseNotConnectException(e);
		}
	}

	private Member getMemberByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(() -> {
			throw new EntityNotFoundException("");
		});
	}

}