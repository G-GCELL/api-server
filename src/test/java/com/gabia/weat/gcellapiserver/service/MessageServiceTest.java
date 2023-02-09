package com.gabia.weat.gcellapiserver.service;

import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabia.weat.gcellapiserver.domain.Member;
import com.gabia.weat.gcellapiserver.domain.type.MessageType;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.repository.MemberRepository;
import com.gabia.weat.gcellapiserver.repository.SseRepository;

@ExtendWith({
	MockitoExtension.class,
	OutputCaptureExtension.class
})
public class MessageServiceTest {

	@Mock
	private SseRepository sseRepository;
	@Mock
	private MemberRepository memberRepository;
	@InjectMocks
	private MessageService messageService;
	private Member member;

	@BeforeEach
	public void setUp() {
		member = Member.builder()
			.memberId(1L)
			.email("test@gabia.com")
			.name("testName")
			.build();
	}

	@Test
	@DisplayName("SSE_연결_성공_테스트")
	public void connect_success_test() throws IOException {
		// given
		String testEmail = member.getEmail();
		SseEmitter testSseEmitter = mock(SseEmitter.class);

		given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));

		// when
		Long memberId = messageService.connect(testEmail, testSseEmitter);

		// then
		assertThat(memberId).isEqualTo(member.getMemberId());
		verify(sseRepository, times(1)).save(any(), any());
		verify(testSseEmitter, times(1)).send(any());
	}

	@Test
	@DisplayName("SSE_연결_실패_사용자_미조회_테스트")
	public void connect_member_not_found_test() {
		// given
		String testEmail = member.getEmail();
		SseEmitter testSseEmitter = mock(SseEmitter.class);

		given(memberRepository.findByEmail(any())).willReturn(Optional.empty());

		// when & then
		assertThrows(CustomException.class, () -> messageService.connect(testEmail, testSseEmitter));
	}

	@Test
	@DisplayName("SSE_연결_실패_IOException_테스트")
	public void connect_exception_test(CapturedOutput capturedOutput) throws IOException {
		// given
		String testEmail = member.getEmail();
		SseEmitter testSseEmitter = mock(SseEmitter.class);

		given(memberRepository.findByEmail(any())).willReturn(Optional.of(member));
		doThrow(IOException.class).when(testSseEmitter).send(any());

		//when
		messageService.connect(testEmail, testSseEmitter);

		// then
		assertThat(capturedOutput.getOut().contains("SEND_MESSAGE_FAIL")).isTrue();
	}

	@Test
	@DisplayName("SSE_메시지_전송_성공_테스트")
	public void sendMessage_success_test() throws IOException {
		// given
		Long memberId = member.getMemberId();
		MessageType messageType = MessageType.FILE_CREATION_COMPLETE;
		SseEmitter testSseEmitter = mock(SseEmitter.class);
		String message = "testMessage";

		given(sseRepository.findById(memberId)).willReturn(Optional.of(testSseEmitter));

		// when
		messageService.sendMessageToMemberId(memberId, messageType, message);

		// then
		verify(testSseEmitter, times(1)).send(any());
	}

	@Test
	@DisplayName("SSE_메시지_전송_실패_sse_미연결_테스트")
	public void sendMessage_not_connect_test() throws IOException {
		// given
		Long memberId = member.getMemberId();
		MessageType messageType = MessageType.FILE_CREATION_COMPLETE;
		SseEmitter testSseEmitter = mock(SseEmitter.class);
		String message = "testMessage";

		given(sseRepository.findById(memberId)).willReturn(Optional.empty());

		// when
		messageService.sendMessageToMemberId(memberId, messageType, message);

		// then
		verify(testSseEmitter, times(0)).send(any());
	}

}