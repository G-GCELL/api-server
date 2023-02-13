package com.gabia.weat.gcellapiserver.service.consumer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.weat.gcellapiserver.domain.type.MessageType;
import com.gabia.weat.gcellapiserver.dto.MessageDto.CreateProgressMsgDto;
import com.rabbitmq.client.Channel;

@ExtendWith(MockitoExtension.class)
public class CreateProgressConsumerTest {

	@Mock
	private Channel channel;
	@InjectMocks
	private CreateProgressConsumer createProgressConsumer;

	@Test
	@DisplayName("엑셀_생성_진행률_메시지_소비_테스트")
	public void receiveMessage_test() throws IOException {
		// given
		CreateProgressMsgDto createProgressMsgDto = this.getCreateProgressMsgDto();
		long tag = 0L;

		// when & then
		assertThatCode(
			() -> createProgressConsumer.receiveMessage(createProgressMsgDto, channel, tag)).doesNotThrowAnyException();
		verify(channel, times(1)).basicAck(eq(tag), anyBoolean());
	}

	private CreateProgressMsgDto getCreateProgressMsgDto() {
		return new CreateProgressMsgDto(
			1L,
			MessageType.FILE_CREATION_PROGRESS,
			"testFileName",
			10
		);
	}

}
