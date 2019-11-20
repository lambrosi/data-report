package com.lucasambrosi.fileprocessor.service;

import com.lucasambrosi.fileprocessor.dto.ReceivedFileMessageDto;
import com.lucasambrosi.fileprocessor.io.FileReader;
import com.lucasambrosi.fileprocessor.io.FileWriter;
import com.lucasambrosi.fileprocessor.messaging.FileProcessedProducer;
import com.lucasambrosi.fileprocessor.parser.FileParser;
import com.lucasambrosi.fileprocessor.report.ReportProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.stream.Stream;

@Service
public class FileProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessorService.class);

    private FileReader fileReader;
    private FileParser fileParser;
    private ReportProcessor reportProcessor;
    private FileWriter fileWriter;
    private FileProcessedProducer fileProcessedProducer;

    public FileProcessorService(FileReader fileReader, FileParser fileParser, ReportProcessor reportProcessor,
                                FileWriter fileWriter, FileProcessedProducer fileProcessedProducer) {
        this.fileReader = fileReader;
        this.fileParser = fileParser;
        this.reportProcessor = reportProcessor;
        this.fileWriter = fileWriter;
        this.fileProcessedProducer = fileProcessedProducer;
    }

    public void processFile(ReceivedFileMessageDto receivedFileMessageDto) {
        LOGGER.info("Starting processing of '{}'", receivedFileMessageDto.getFilename());
        try {
            File file = new File(receivedFileMessageDto.getFilename());
            Stream.of(file)
                    .map(fileReader::extractContent)
                    .map(fileParser::processContent)
                    .map(reportProcessor::generateReport)
                    .map(fileWriter::writeReport)
                    .forEach(fileProcessedProducer::sendSuccessMessageToKafka);

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
        }
    }
}
