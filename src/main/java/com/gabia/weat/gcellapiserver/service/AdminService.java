package com.gabia.weat.gcellapiserver.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.weat.gcellapiserver.dto.FileDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellapiserver.dto.MessageDto.CsvUpdateRequestMsgDto;
import com.gabia.weat.gcellapiserver.dto.MessageWrapperDto;
import com.gabia.weat.gcellapiserver.dto.log.LogFormatFactory;
import com.gabia.weat.gcellapiserver.error.ErrorCode;
import com.gabia.weat.gcellapiserver.error.exception.CustomException;
import com.gabia.weat.gcellapiserver.service.producer.CsvUpdateRequestProducer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final String CSV_FILE_EXTENSION = ".csv";
	private final MinioService minioService;
	private final CsvUpdateRequestProducer csvUpdateRequestProducer;
	private final LogFormatFactory logFormatFactory;

	public void updateCsvFile(CsvUpdateRequestDto csvUpdateRequestDto) {
		validFileExtension(csvUpdateRequestDto.file());
		String fileName = addCurrentTimePrefix(csvUpdateRequestDto.file().getOriginalFilename());
		minioService.upload(csvUpdateRequestDto.file(), fileName);
		csvUpdateRequestProducer.sendMessage(MessageWrapperDto.wrapMessageDto(
			new CsvUpdateRequestMsgDto(fileName, csvUpdateRequestDto.deleteTarget()),
			logFormatFactory.getTraceId()
		));
	}

	private String addCurrentTimePrefix(String content) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")) + "_" + content;
	}

	private void validFileExtension(MultipartFile file){
		if (!file.getOriginalFilename().endsWith(CSV_FILE_EXTENSION)) {
			throw new CustomException(ErrorCode.INVALID_FILE_EXTENSION);
		}
	}

}
