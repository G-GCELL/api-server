package com.gabia.weat.gcellapiserver.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.gabia.weat.gcellapiserver.repository.MemberRepository;
import com.gabia.weat.gcellapiserver.repository.SseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseService {

	private final SseRepository sseRepository;
	private final MemberRepository memberRepository;

	public Long connect(String email, SseEmitter sseEmitter) {
		return null;
	}

	public void sendMessage(Long memberId, String message) {

	}

}