package com.gabia.weat.gcellapiserver.controller;

import static org.mockito.BDDMockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.gabia.weat.gcellapiserver.service.MessageSender;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {

	@Mock
	private MessageSender messageSender;
	@InjectMocks
	private MessageController messageController;

	@BeforeEach
	void setUp() {
		UserDetails user = this.createUserDetails();

		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));
	}

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

	private UserDetails createUserDetails() {
		return new User("test@email.com", "",
			Arrays.stream("MEMBER".split(",")).map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList());
	}

}
