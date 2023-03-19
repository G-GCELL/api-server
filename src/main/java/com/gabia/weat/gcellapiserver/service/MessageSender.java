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
public class MessageSender {

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

	public SseEmitter registryFileConnection(Long excelInfoId, SseEmitter sseEmitter){
		return sseRepository.registryFileConnection(excelInfoId, sseEmitter);
	}

	public void sendMessageToMemberId(Long memberId, MessageType messageType, Object message) {
		sseRepository.findListById(memberId).forEach(sse -> this.sendMessage(message, messageType, sse));
	}

	public void sendMessageToExcelInfoId(Long excelInfoId, MessageType messageType, Object message){
		sseRepository.findByExcelInfoId(excelInfoId).ifPresent(sse -> this.sendMessage(message, messageType, sse));
	}

	public void disconnectByExcelInfoId(Long excelInfoId){
		sseRepository.deleteByExcelInfoId(excelInfoId);
	}

	private void sendMessage(Object message, MessageType messageType, SseEmitter sseEmitter) {
		try {
			sseEmitter.send(SseEmitter.event()
				.name(messageType.name())
				.data(message));
		} catch (IOException | IllegalStateException e) {
			log.debug("SEND_MESSAGE_FAIL : {}", e.getMessage());
			sseEmitter.complete();
		}
	}

	private Member getMemberByEmail(String email) {
		return memberRepository.findByEmail(email).orElseThrow(() -> {
			throw new CustomException(ErrorCode.CONNECTION_NOT_FOUND);
		});
	}

}
