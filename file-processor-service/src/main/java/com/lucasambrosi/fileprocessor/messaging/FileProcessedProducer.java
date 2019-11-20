package com.lucasambrosi.fileprocessor.messaging;

import com.lucasambrosi.fileprocessor.dto.ReportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class FileProcessedProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessedProducer.class);

    private KafkaTemplate<String, Object> kafkaTemplate;
    private String fileProcessedTopic;

    public FileProcessedProducer(
            @Value("${application.messaging.topic.file-processed}") String fileProcessedTopic,
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.fileProcessedTopic = fileProcessedTopic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendSuccessMessageToKafka(ReportDto message) {
        try {
            kafkaTemplate.send(fileProcessedTopic, message);
            LOGGER.info("Message for file '{}' sent to topic '{}' in Kafka.", message.getFilename(), fileProcessedTopic);
        } catch (Exception ex) {
            LOGGER.error("Error to send message to Kafka for file '{}'", message.getFilename());
        }
    }
}
