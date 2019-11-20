package com.lucasambrosi.fileprocessor.messaging;

import com.lucasambrosi.fileprocessor.dto.ReceivedFileMessageDto;
import com.lucasambrosi.fileprocessor.service.FileProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceivedFileConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReceivedFileConsumer.class);

    private FileProcessorService fileProcessorService;

    public ReceivedFileConsumer(FileProcessorService fileProcessorService) {
        this.fileProcessorService = fileProcessorService;
    }

    @RabbitListener(queues = "${application.messaging.queue}")
    protected void consume(ReceivedFileMessageDto dto) {
        LOGGER.info("Received message for file: {}", dto.getFilename());
        try {
            fileProcessorService.processFile(dto);
        } catch (Exception ex) {
            final String ERROR_MESSAGE = String.format("Error processing file '%s'.", dto.getFilename());

            LOGGER.error(ERROR_MESSAGE);
            throw new RuntimeException(ERROR_MESSAGE, ex);
        }
        LOGGER.info("File '{}' processed successfully!\n", dto.getFilename());
    }
}
