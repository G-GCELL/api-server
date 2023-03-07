package com.gabia.weat.gcellapiserver.service.log;

import static com.gabia.weat.gcellapiserver.dto.log.ApiLogFormatDto.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.event.Level;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpMethod;

import com.gabia.weat.gcellapiserver.domain.type.TargetType;
import com.gabia.weat.gcellapiserver.dto.log.ErrorLogFormatDto;
import com.gabia.weat.gcellapiserver.dto.log.LogFormatFactory;
import com.gabia.weat.gcellapiserver.dto.log.MessageBrokerLogFormatDto;
import com.gabia.weat.gcellapiserver.parser.CustomExpressionParser;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
public class ConsoleLogPrinterTest {

	@Mock
	private LogFormatFactory logFormatFactory;
	@Mock
	private CustomExpressionParser expressionParser;
	@InjectMocks
	private ConsoleLogPrinter logPrinter;

	@Test
	@DisplayName("API_로그_테스트")
	public void printApiLog(CapturedOutput output){
		// given
		given(logFormatFactory.getApiLogFormatBuilder()).willReturn(this.getApiLogFormatBuilder());
		HttpServletRequest testRequest = mock(HttpServletRequest.class);

		given(testRequest.getMethod()).willReturn(String.valueOf(HttpMethod.GET));
		given(testRequest.getRequestURI()).willReturn("testUri");
		given(testRequest.getHeader(any())).willReturn("testHeader");

		// when
		logPrinter.printApiLog(testRequest,true, 200, 0, 100, "testInput");

		// then
		assertThat(output.getOut().contains("testUri")).isTrue();
	}

	@Test
	@DisplayName("에러_로그_테스트")
	public void printErrorLog_test(CapturedOutput output){
		// given
		Exception exception = new RuntimeException("test");
		given(logFormatFactory.getErrorLogFormatBuilder()).willReturn(this.getErrorLogFormatBuilder());

		// when
		logPrinter.printErrorLog(exception);

		// then
		assertThat(output.getOut().contains("message: test")).isTrue();
	}

	@Test
	@DisplayName("메시지_브로커_로그_테스트")
	public void printMessageBrokerLog_test(CapturedOutput output){
		// given
		given(logFormatFactory.getMessageBrokerLogFormatBuilder()).willReturn(this.getMessageBrokerLogFormatBuilder());
		given(expressionParser.parse(any())).willReturn("testQueue");

		// when
		logPrinter.printMessageBrokerLog(TargetType.CONSUMER, "testQueue", "testInput", null);

		// then
		assertThat(output.getOut().contains("(testQueue)")).isTrue();
	}

	public ApiLogFormatDtoBuilder getApiLogFormatBuilder() {
		return builder()
			.level(Level.INFO)
			.serverName("serverName")
			.traceId("testTraceId");
	}

	private ErrorLogFormatDto.ErrorLogFormatDtoBuilder getErrorLogFormatBuilder() {
		return ErrorLogFormatDto.builder()
			.level(Level.ERROR)
			.serverName("serverName")
			.traceId("testTraceId");
	}

	private MessageBrokerLogFormatDto.MessageBrokerLogFormatDtoBuilder getMessageBrokerLogFormatBuilder() {
		return MessageBrokerLogFormatDto.builder()
			.level(Level.INFO)
			.serverName("serverName")
			.traceId("testTraceId");
	}

}