package com.gabia.weat.gcellapiserver.controller;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.weat.gcellapiserver.service.MessageService;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

	@Mock
	private MessageService messageService;
	@InjectMocks
	private MessageController messageController;

	@Test
	@DisplayName("연결_테스트")
	public void connect_test() {
		// given
		given(messageService.connect(any(), any())).willReturn(1L);

		// when
		messageController.connect();

		// then
		verify(messageService, times(1)).connect(any(), any());
	}

}