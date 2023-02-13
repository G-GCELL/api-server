package com.gabia.weat.gcellapiserver.service.producer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateRequestMsgDto;

@ExtendWith(MockitoExtension.class)
public class CreateRequestProducerTest {

	@Mock
	private RabbitTemplate rabbitTemplate;

	@InjectMocks
	private CreateRequestProducer createRequestProducer;

	@Test
	@DisplayName("엑셀_생성_요청_메시지_발행_테스트")
	public void sendMessage_test() {
		//given
		FileCreateRequestMsgDto fileCreateRequestMsgDto = this.getFileCreateRequestMsgDTO();

		// when & then
		assertThatCode(() -> createRequestProducer.sendMessage(fileCreateRequestMsgDto)).doesNotThrowAnyException();
		verify(rabbitTemplate, times(1)).correlationConvertAndSend(eq(fileCreateRequestMsgDto),
			any(CorrelationData.class));
	}

	private FileCreateRequestMsgDto getFileCreateRequestMsgDTO() {
		return new FileCreateRequestMsgDto(
			1L,
			"testFileName",
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

}
