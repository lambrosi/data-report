package com.lucasambrosi.directorymonitor.messaging;

import com.lucasambrosi.directorymonitor.dto.FileReceivedMessageDto;
import com.lucasambrosi.directorymonitor.exception.RabbitSendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileReceivedMessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReceivedMessageProducer.class);
    private RabbitTemplate rabbitTemplate;
    private String outputExchange;

    public FileReceivedMessageProducer(RabbitTemplate rabbitTemplate,
                                       @Value("${application.messaging.output.exchange}") String outputExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.outputExchange = outputExchange;
    }

    public void sendFileReceivedMessage(FileReceivedMessageDto fileReceivedMessageDto) {
        LOGGER.info("Sending message to exchange {} for file {}.", outputExchange, fileReceivedMessageDto.getFilename());
        try {
            rabbitTemplate.convertAndSend(outputExchange, "#", fileReceivedMessageDto);
        } catch (Exception ex) {
            LOGGER.info("Error to send message to exchange {} for file {}.", outputExchange, fileReceivedMessageDto.getFilename());
            throw new RabbitSendException(fileReceivedMessageDto.getFilename(), ex.getMessage());
        }
    }
}
