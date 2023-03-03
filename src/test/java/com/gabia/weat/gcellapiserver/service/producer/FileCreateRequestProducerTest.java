package com.gabia.weat.gcellapiserver.service.producer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateRequestMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;

@ExtendWith(MockitoExtension.class)
public class FileCreateRequestProducerTest {

	@Mock
	private RabbitTemplate rabbitTemplate;

	@InjectMocks
	private FileCreateRequestProducer fileCreateRequestProducer;

	@Test
	@DisplayName("엑셀_생성_요청_메시지_발행_테스트")
	public void sendMessage_test() {
		//given
		FileCreateRequestMsgDto fileCreateRequestMsgDto = this.getFileCreateRequestMsgDTO();
		String traceId = "testid";
		MessageWrapperDto<FileCreateRequestMsgDto> messageWrapperDto = this.getMessageWrapperDto(
			fileCreateRequestMsgDto, traceId);

		// when & then
		assertThatCode(() -> fileCreateRequestProducer.sendMessage(messageWrapperDto)).doesNotThrowAnyException();
		verify(rabbitTemplate, times(1)).correlationConvertAndSend(eq(messageWrapperDto),
			any(CorrelationData.class));
	}

	private FileCreateRequestMsgDto getFileCreateRequestMsgDTO() {
		return new FileCreateRequestMsgDto(
			1L,
			1L,
			"testFileName",
			List.of("testColumn"),
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null
		);
	}

	private MessageWrapperDto<FileCreateRequestMsgDto> getMessageWrapperDto(FileCreateRequestMsgDto messageDto,
		String traceId) {
		return new MessageWrapperDto<>(messageDto, traceId);
	}

}