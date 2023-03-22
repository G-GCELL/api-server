package com.gabia.weat.gcellapiserver.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gabia.weat.gcellapiserver.dto.FileDto.CsvUpdateRequestDto;
import com.gabia.weat.gcellapiserver.service.producer.CsvUpdateRequestProducer;
import com.gabia.weat.gcellcommonmodule.dto.MessageDto.CsvUpdateRequestMsgDto;
import com.gabia.weat.gcellcommonmodule.dto.MessageWrapperDto;
import com.gabia.weat.gcellcommonmodule.dto.format.LogFormatFactory;
import com.gabia.weat.gcellcommonmodule.error.ErrorCode;
import com.gabia.weat.gcellcommonmodule.error.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final String CSV_FILE_EXTENSION = ".csv";
	private final MinioService minioService;
	private final CsvUpdateRequestProducer csvUpdateRequestProducer;
	private final LogFormatFactory logFormatFactory;

	public void updateCsvFile(String email, CsvUpdateRequestDto csvUpdateRequestDto) {
		validFileExtension(csvUpdateRequestDto.file());
		String fileName = addCurrentTimePrefix(csvUpdateRequestDto.file().getOriginalFilename());
		minioService.upload(csvUpdateRequestDto.file(), fileName);
		csvUpdateRequestProducer.sendMessage(MessageWrapperDto.wrapMessageDto(
			new CsvUpdateRequestMsgDto(fileName, email, csvUpdateRequestDto.deleteTarget()),
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