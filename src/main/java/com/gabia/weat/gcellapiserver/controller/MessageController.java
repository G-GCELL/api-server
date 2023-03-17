package com.gabia.weat.gcellapiserver.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.gabia.weat.gcellapiserver.service.MessageSender;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageController {

	@Value("${sse.timeout}")
	private long sseTimeOut;
	private final MessageSender messageSender;


	@GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> connectProgress() {
		SseEmitter sseEmitter = new SseEmitter(sseTimeOut);
		messageSender.connect(this.getConnectMemberEmail(), sseEmitter);
		return ResponseEntity.ok(sseEmitter);
	}

	@GetMapping(value = "/connect/file/{excel-info-id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> connectComplete(@PathVariable("excel-info-id") Long excelInfoId) {
		SseEmitter sseEmitter = new SseEmitter(sseTimeOut);
		messageSender.registryFileConnection(excelInfoId, sseEmitter);
		return ResponseEntity.ok(sseEmitter);
	}

	private String getConnectMemberEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}
