package com.gabia.weat.gcellapiserver.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.gabia.weat.gcellapiserver.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageController {
	private final MessageService messageService;

	@CrossOrigin("*")
	@GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public ResponseEntity<SseEmitter> connect() {
		SseEmitter sseEmitter = new SseEmitter(60000L);
		messageService.connect(this.getConnectMemberEmail(), sseEmitter);
		return ResponseEntity.ok(sseEmitter);
	}

	private String getConnectMemberEmail() {
		return "mock_email";
	}

}