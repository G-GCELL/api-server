package com.gabia.weat.gcellapiserver.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.domain.type.MessageType;
import com.gabia.weat.gcellapiserver.error.exception.SseNotConnectException;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;
import com.gabia.weat.gcellapiserver.repository.SseRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final SseRepository sseRepository;
	private final MemberRepository memberRepository;

	public Long connect(String email, SseEmitter sseEmitter) {
		Member member = this.getMemberByEmail(email);
		Long memberId = member.getMemberId();
		sseRepository.save(memberId, sseEmitter);
		sendMessage("connect", MessageType.CONNECT, sseEmitter);
		return memberId;
	}

	public void sendMessageToMemberId(Long memberId, MessageType messageType, Object message) {
		sseRepository.findById(memberId).ifPresent(sse -> sendMessage(message, messageType, sse));
	}

	private void sendMessage(Object message, MessageType messageType, SseEmitter sseEmitter) {
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