package com.lucasambrosi.fileprocessor.service;

import com.lucasambrosi.fileprocessor.dto.FileContentDto;
import com.lucasambrosi.fileprocessor.dto.ReceivedFileMessageDto;
import com.lucasambrosi.fileprocessor.dto.ReportDto;
import com.lucasambrosi.fileprocessor.io.FileReader;
import com.lucasambrosi.fileprocessor.io.FileWriter;
import com.lucasambrosi.fileprocessor.messaging.FileProcessedProducer;
import com.lucasambrosi.fileprocessor.model.Model;
import com.lucasambrosi.fileprocessor.parser.FileParser;
import com.lucasambrosi.fileprocessor.report.ReportProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import util.TestUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
public class FileProcessorServiceTest {

    private static final String FILENAME = "filename";

    @InjectMocks
    private FileProcessorService fileProcessorService;

    @Mock
    private FileReader fileReader;
    @Mock
    private FileParser fileParser;
    @Mock
    private ReportProcessor reportProcessor;
    @Mock
    private FileWriter fileWriter;
    @Mock
    private FileProcessedProducer fileProcessedProducer;

    @Test
    public void fileParserArgumentReceivedTest() {
        FileContentDto<String> fileContentDto = new FileContentDto<>(FILENAME, getFileContent());
        Mockito.when(fileReader.extractContent(Mockito.any()))
                .thenReturn(fileContentDto);

        fileProcessorService.processFile(buildReceivedFileMessageDto());

        ArgumentCaptor<FileContentDto> fileContentCaptor = ArgumentCaptor.forClass(FileContentDto.class);
        Mockito.verify(fileParser).processContent(fileContentCaptor.capture());
        FileContentDto<String> capturedFileContent = fileContentCaptor.getValue();

        Assertions.assertEquals(fileContentDto, capturedFileContent);
    }

    @Test
    public void reportProcessorArgumentReceivedTest() {
        FileContentDto<Model> fileContentParsed = parseContentString(new FileContentDto<>(FILENAME, getFileContent()));
        Mockito.when(fileParser.processContent(Mockito.any())).thenReturn(fileContentParsed);

        fileProcessorService.processFile(buildReceivedFileMessageDto());

        ArgumentCaptor<FileContentDto> fileContentCaptor = ArgumentCaptor.forClass(FileContentDto.class);
        Mockito.verify(reportProcessor).generateReport(fileContentCaptor.capture());
        FileContentDto<Model> fileContentCaptured = fileContentCaptor.getValue();

        Assertions.assertEquals(fileContentParsed, fileContentCaptured);
    }

    @Test
    void fileWriterArgumentReceivedTest() {
        mockWriteReport();
        ReportDto reportDto = new ReportDto("filename", 2L, 2L, 10L, "Pedro");
        Mockito.when(reportProcessor.generateReport(Mockito.any())).thenReturn(reportDto);

        fileProcessorService.processFile(buildReceivedFileMessageDto());

        ArgumentCaptor<ReportDto> reportCaptor = ArgumentCaptor.forClass(ReportDto.class);
        Mockito.verify(fileWriter).writeReport(reportCaptor.capture());
        ReportDto capturedReportDto = reportCaptor.getValue();

        Assertions.assertEquals(reportDto, capturedReportDto);
    }

    @Test
    void processFileWithExceptionShouldNotStopTheApplicationTest() {
        mockExtractContentError();
        fileProcessorService.processFile(buildReceivedFileMessageDto());
    }

    @Test
    void sendSuccessMessageToKafkaTest() {
        mockWriteReport();

        ReceivedFileMessageDto dto = buildReceivedFileMessageDto();
        fileProcessorService.processFile(dto);

        ArgumentCaptor<ReportDto> reportCaptor = ArgumentCaptor.forClass(ReportDto.class);
        Mockito.verify(fileProcessedProducer).sendSuccessMessageToKafka(reportCaptor.capture());

        ReportDto capturedDto = reportCaptor.getValue();
        Assertions.assertEquals(dto.getFilename(), capturedDto.getFilename());
    }

    private ReceivedFileMessageDto buildReceivedFileMessageDto() {
        ReceivedFileMessageDto dto = new ReceivedFileMessageDto();
        dto.setFilename("filename");
        dto.setReceivedDate(LocalDateTime.now());
        return dto;
    }

    private void mockWriteReport() {
        Mockito.when(fileWriter.writeReport(Mockito.any())).thenReturn(new ReportDto("filename", 5L, 2L, 3L, ""));
    }

    private void mockExtractContentError() {
        Mockito.doThrow(new RuntimeException()).when(fileReader).extractContent(Mockito.any());
    }

    private List<String> getFileContent() {
        return Arrays.asList(
                "001ç1234567891234çPedroç50000",
                "001ç3245678865434çPauloç40000.99",
                "002ç2345675434544345çJose da SilvaçRural",
                "002ç2345675433444345çEduardo PereiraçRural",
                "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro",
                "003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo"
        );
    }

    private FileContentDto<Model> parseContentString(FileContentDto<String> rawContent) {
        return TestUtil.getFileParser().processContent(rawContent);
    }
}
