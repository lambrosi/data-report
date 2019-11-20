package com.lucasambrosi.directorymonitor.service;

import com.lucasambrosi.directorymonitor.dto.FileReceivedMessageDto;
import com.lucasambrosi.directorymonitor.exception.RabbitSendException;
import com.lucasambrosi.directorymonitor.messaging.FileReceivedMessageProducer;
import com.lucasambrosi.directorymonitor.util.FileSystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DirectoryMonitorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DirectoryMonitorService.class);

    private FileReceivedMessageProducer fileReceivedMessageProducer;
    private Path pathInputDirectory;
    private String inputDirectory;
    private String processedDirectory;
    private String fileExtension;


    public DirectoryMonitorService(FileReceivedMessageProducer fileReceivedMessageProducer,
                                   @Value("${application.monitoring.input-directory}") String inputDirectory,
                                   @Value("${application.monitoring.processed-directory}")String processedDirectory,
                                   @Value("${application.monitoring.file-extension}")String fileExtension) {
        this.inputDirectory = inputDirectory;
        this.processedDirectory = processedDirectory;
        this.fileExtension = fileExtension;
        this.fileReceivedMessageProducer = fileReceivedMessageProducer;

        LOGGER.info("Creating needed directories.");
        FileSystemUtil.createDirectoriesInUserHome(inputDirectory, processedDirectory);

        LOGGER.info("Monitoring directory '{}'.", inputDirectory);
        pathInputDirectory = Paths.get(inputDirectory);
    }

    public void verifyInputDirectory() {
        try {
            Files.list(pathInputDirectory)
                    .map(Path::toFile)
                    .filter(this::filterAllowedFile)
                    .map(this::moveToProcessed)
                    .map(this::mapToFileReceivedMessageDto)
                    .forEach(fileReceivedMessageProducer::sendFileReceivedMessage);

        } catch (RabbitSendException ex) {
            this.moveFileBackToInputDirectoryDueRabbitError(ex.getFilename());
        }catch (IOException ex) {
            LOGGER.error("Error on list files in input directory {}", inputDirectory);
            throw new RuntimeException(ex);
        }
    }

    private boolean filterAllowedFile(File file) {
        return file.getName().toLowerCase().endsWith(fileExtension);
    }

    private File moveToProcessed(File file) {
        return FileSystemUtil.moveFileTo(file, processedDirectory);
    }

    private void moveFileBackToInputDirectoryDueRabbitError(String filename) {
        File file = Paths.get(processedDirectory, filename).toFile();
        FileSystemUtil.moveFileTo(file, inputDirectory);
    }

    private FileReceivedMessageDto mapToFileReceivedMessageDto(File file) {
        return new FileReceivedMessageDto(file.getAbsolutePath());
    }
}
