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

import com.gabia.weat.gcellapiserver.domain.type.MessageType;
import com.gabia.weat.gcellapiserver.dto.MessageDto;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateProgressMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellapiserver.service.MessageService;
import com.rabbitmq.client.Channel;

@ExtendWith(MockitoExtension.class)
public class FileCreateProgressConsumerTest {

	@Mock
	private Channel channel;
	@Mock
	private MessageService messageService;
	@InjectMocks
	private FileCreateProgressConsumer fileCreateProgressConsumer;

	@Test
	@DisplayName("엑셀_생성_진행률_메시지_소비_테스트")
	public void receiveMessage_test() throws IOException {
		// given
		String traceId = "testid";
		long tag = 0L;
		FileCreateProgressMsgDto fileCreateProgressMsgDto = this.getCreateProgressMsgDto();
		MessageWrapperDto<FileCreateProgressMsgDto> messageWrapperDto = this.getMessageWrapperDto(
			fileCreateProgressMsgDto, traceId);

		// when & then
		assertThatCode(
			() -> fileCreateProgressConsumer.receiveMessage(messageWrapperDto, channel,
				tag)).doesNotThrowAnyException();
		verify(messageService, times(1)).sendMessageToMemberId(any(), any(), any());
		verify(channel, times(1)).basicAck(eq(tag), anyBoolean());
	}

	private FileCreateProgressMsgDto getCreateProgressMsgDto() {
		return new FileCreateProgressMsgDto(
			1L,
			MessageType.FILE_CREATION_PROGRESS,
			"testFileName",
			10
		);
	}

	private MessageWrapperDto<FileCreateProgressMsgDto> getMessageWrapperDto(
		FileCreateProgressMsgDto messageDto,
		String traceId) {
		return new MessageWrapperDto<>(messageDto, traceId);
	}

}