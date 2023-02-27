package com.gabia.weat.gcellapiserver.service;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabia.weat.gcellapiserver.domain.ExcelInfo;
import com.gabia.weat.gcellapiserver.domain.type.MessageType;
import com.gabia.weat.gcellapiserver.dto.MessageDto.FileCreateProgressMsgDto;
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
		ExcelInfo excelInfo = ExcelInfo.builder()
			.excelInfoId(1L)
			.name("test")
			.build();
		given(excelInfoRepository.findById(any())).willReturn(Optional.ofNullable(excelInfo));

		// when
		messageHandler.sendCreateExcelMsg(messageWrapperDto);

		// then
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