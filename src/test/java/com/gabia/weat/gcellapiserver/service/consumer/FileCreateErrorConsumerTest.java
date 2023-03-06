package com.gabia.weat.gcellapiserver.service.consumer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateErrorMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellapiserver.service.MessageHandler;

@ExtendWith(MockitoExtension.class)
public class FileCreateErrorConsumerTest {

	@Mock
	MessageHandler messageHandler;
	@InjectMocks
	FileCreateErrorConsumer fileCreateErrorConsumer;

	@Test
	@DisplayName("엑셀_파일_생성_실패_메시지_소비_테스트")
	public void receiveMessage_test() throws IOException {
		// given
		String traceId = "testid";
		long tag = 0L;
		FileCreateErrorMsgDto fileCreateErrorMsgDto = this.getFileCreateErrorMsgDto();
		MessageWrapperDto<FileCreateErrorMsgDto> messageWrapperDto = this.getMessageWrapperDto(fileCreateErrorMsgDto,
			traceId);

		// when & then
		assertThatCode(
			() -> fileCreateErrorConsumer.receiveMessage(messageWrapperDto)).doesNotThrowAnyException();
		verify(messageHandler, times(1)).sendErrorMsg(any());
	}

	private FileCreateErrorMsgDto getFileCreateErrorMsgDto() {
		return new FileCreateErrorMsgDto(
			1L,
			1L,
			10000,
			"error message"
		);
	}

	private MessageWrapperDto<FileCreateErrorMsgDto> getMessageWrapperDto(FileCreateErrorMsgDto messageDto,
		String traceId) {
		return new MessageWrapperDto<>(messageDto, traceId);
	}

}
