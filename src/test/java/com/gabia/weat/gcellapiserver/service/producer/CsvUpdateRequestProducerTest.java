package com.gabia.weat.gcellapiserver.service.producer;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.YearMonth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import com.gabia.weat.gcellapiserver.domain.type.JobType;
import com.gabia.weat.gcellapiserver.dto.MessageDto.CsvUpdateRequestMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;

@ExtendWith(MockitoExtension.class)
public class CsvUpdateRequestProducerTest {

	@Mock
	private RabbitTemplate csvUpdateRequestRabbitTemplate;
	@InjectMocks
	private CsvUpdateRequestProducer csvUpdateRequestProducer;

	@Test
	@DisplayName("데이터_수정_요청_메시지_발행_테스트")
	public void sendMessage_test() {
		//given
		CsvUpdateRequestMsgDto csvUpdateRequestMsgDto = this.getCsvUpdateRequestMsgDto();
		String traceId = "testid";
		MessageWrapperDto<CsvUpdateRequestMsgDto> messageWrapperDto = this.getMessageWrapperDto(
			csvUpdateRequestMsgDto, traceId);

		// when & then
		assertThatCode(() -> csvUpdateRequestProducer.sendMessage(messageWrapperDto)).doesNotThrowAnyException();
		verify(csvUpdateRequestRabbitTemplate, times(1)).correlationConvertAndSend(eq(messageWrapperDto),
			any(CorrelationData.class));
	}

	private CsvUpdateRequestMsgDto getCsvUpdateRequestMsgDto() {
		return new CsvUpdateRequestMsgDto(
			"testLocate",
			YearMonth.now(),
			JobType.MANUAL
		);
	}

	private MessageWrapperDto<CsvUpdateRequestMsgDto> getMessageWrapperDto(
		CsvUpdateRequestMsgDto messageDto,
		String traceId) {
		return new MessageWrapperDto<>(messageDto, traceId);
	}

}