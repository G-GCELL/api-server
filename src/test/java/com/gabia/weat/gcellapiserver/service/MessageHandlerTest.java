package com.gabia.weat.gcellapiserver.service;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.type.MessageType;
import com.gabia.weat.gcellapiserver.dto.MessageDto;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateProgressMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateRequestErrorMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellapiserver.repository.ExcelInfoRepository;

@ExtendWith(MockitoExtension.class)
public class MessageHandlerTest {

	@Mock
	private MessageSender messageSender;
	@Mock
	private ExcelInfoRepository excelInfoRepository;
	@InjectMocks
	private MessageHandler messageHandler;

	private ExcelInfo excelInfo;

	@BeforeEach
	public void setUp(){
		excelInfo = ExcelInfo.builder()
			.excelInfoId(1L)
			.name("test")
			.build();
	}

	@Test
	@DisplayName("엑셀_생성_진행률_메시지_전송_테스트")
	public void sendCreateExcelMsg_progress_test(){
		// given
		FileCreateProgressMsgDto fileCreateProgressMsgDto = this.getFileCreateProgressMsgDto(MessageType.FILE_CREATION_PROGRESS);
		MessageWrapperDto messageWrapperDto = MessageWrapperDto.wrapMessageDto(fileCreateProgressMsgDto, "");

		// when
		messageHandler.sendCreateExcelMsg(messageWrapperDto);

		// then
		verify(messageSender, times(1)).sendMessageToMemberId(any(), any(), any());
	}

	@Test
	@DisplayName("엑셀_생성_완료_메시지_전송_테스트")
	public void sendCreateExcelMsg_complete_test(){
		// given
		FileCreateProgressMsgDto fileCreateProgressMsgDto = this.getFileCreateProgressMsgDto(MessageType.FILE_CREATION_COMPLETE);
		MessageWrapperDto messageWrapperDto = MessageWrapperDto.wrapMessageDto(fileCreateProgressMsgDto, "");

		given(excelInfoRepository.findById(any())).willReturn(Optional.ofNullable(excelInfo));

		// when
		messageHandler.sendCreateExcelMsg(messageWrapperDto);

		// then
		verify(messageSender, times(1)).sendMessageToExcelInfoId(any(), any(), any());
	}

	@Test
	@DisplayName("에러_메시지_전송_테스트")
	public void sendErrorMsg_test(){
		// given
		FileCreateRequestErrorMsgDto fileCreateRequestErrorMsgDto = new FileCreateRequestErrorMsgDto(
			1L,
			excelInfo.getExcelInfoId(),
			404,
			"test_message"
		);

		MessageWrapperDto messageWrapperDto = MessageWrapperDto.wrapMessageDto(fileCreateRequestErrorMsgDto, "");

		given(excelInfoRepository.findById(any())).willReturn(Optional.of(excelInfo));

		// when
		messageHandler.sendErrorMsg(messageWrapperDto);

		// then
		verify(excelInfoRepository, times(1)).delete(any());
		verify(messageSender, times(1)).sendMessageToExcelInfoId(any(), any(), any());
	}

	private FileCreateProgressMsgDto getFileCreateProgressMsgDto(MessageType messageType){
		return new FileCreateProgressMsgDto(
			1L,
			1L,
			messageType,
			"test",
			0
		);
	}

}