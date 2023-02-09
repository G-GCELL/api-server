package com.gabia.weat.gcellapiserver.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.domain.type.MessageType;
import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;
import com.gabia.weat.gcellapiserver.repository.SseRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

	private static final String CONNECT_MESSAGE = "connect_success";
	private final SseRepository sseRepository;
	private final MemberRepository memberRepository;

	public Long connect(String email, SseEmitter sseEmitter) {
		Member member = this.getMemberByEmail(email);
		Long memberId = member.getMemberId();
		sseRepository.save(memberId, sseEmitter);
		sendMessage(CONNECT_MESSAGE, MessageType.CONNECT, sseEmitter);
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
			log.info("SEND_MESSAGE_FAIL : " + e.getMessage());
		}
	}

	private Member getMemberByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(() -> {
			throw new CustomException(ErrorCode.CONNECTION_NOT_FOUND);
		});
	}

}