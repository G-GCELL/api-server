package com.gabia.weat.gcellapiserver.controller;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.weat.gcellapiserver.service.MessageSender;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

	@Mock
	private MessageSender messageSender;
	@InjectMocks
	private MessageController messageController;

	@Test
	@DisplayName("연결_테스트")
	public void connect_test() {
		// given
		given(messageSender.connect(any(), any())).willReturn(1L);

		// when
		messageController.connectProgress();

		// then
		verify(messageSender, times(1)).connect(any(), any());
	}

}