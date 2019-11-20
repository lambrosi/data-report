package com.lucasambrosi.directorymonitor.service;

import com.lucasambrosi.directorymonitor.dto.FileReceivedMessageDto;
import com.lucasambrosi.directorymonitor.exception.RabbitSendException;
import com.lucasambrosi.directorymonitor.messaging.FileReceivedMessageProducer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
public class DirectoryMonitorServiceTest {

    private static final String TEMP_DIRECTORY = "/tmp";

    private DirectoryMonitorService directoryMonitorService;
    @Mock
    private FileReceivedMessageProducer fileReceivedMessageProducer;

    @Test
    void verifyInputDirectoryTest() throws IOException {
        mockRabbitProducerBehavior();
        final String INPUT_DIRECTORY = TEMP_DIRECTORY + "/in1";
        final String PROCESSED_DIRECTORY = TEMP_DIRECTORY + "/pr1";

        directoryMonitorService = new DirectoryMonitorService(
                fileReceivedMessageProducer, INPUT_DIRECTORY, PROCESSED_DIRECTORY, ".dat");

        createTempFileInInputDirectory(INPUT_DIRECTORY, "file1.dat", "file2.dat");

        directoryMonitorService.verifyInputDirectory();

        ArgumentCaptor<FileReceivedMessageDto> fileReceivedDtoCaptor = ArgumentCaptor.forClass(FileReceivedMessageDto.class);
        Mockito.verify(fileReceivedMessageProducer, Mockito.times(2)).sendFileReceivedMessage(fileReceivedDtoCaptor.capture());

        List<String> filenamesCaptured = fileReceivedDtoCaptor.getAllValues().stream().map(FileReceivedMessageDto::getFilename).collect(Collectors.toList());
        List<String> filenamesExpected = Arrays.asList(PROCESSED_DIRECTORY + File.separator + "file1.dat",
                PROCESSED_DIRECTORY + File.separator + "file2.dat");
        Assertions.assertTrue(filenamesExpected.contains(filenamesCaptured.get(0)));
        Assertions.assertTrue(filenamesExpected.contains(filenamesCaptured.get(1)));
        Assertions.assertEquals(LocalDate.now(), fileReceivedDtoCaptor.getAllValues().get(0).getReceivedDate().toLocalDate());
        Assertions.assertEquals(LocalDate.now(), fileReceivedDtoCaptor.getAllValues().get(1).getReceivedDate().toLocalDate());

        removeDirectory(INPUT_DIRECTORY, PROCESSED_DIRECTORY);
    }

    @Test
    void verifyInputDirectoryWithFileWithWrongExtensionTest() throws IOException {
        final String INPUT_DIRECTORY = TEMP_DIRECTORY + "/in2";
        final String PROCESSED_DIRECTORY = TEMP_DIRECTORY + "/pr2";

        directoryMonitorService = new DirectoryMonitorService(
                fileReceivedMessageProducer, INPUT_DIRECTORY, PROCESSED_DIRECTORY, ".dat");

        createTempFileInInputDirectory(INPUT_DIRECTORY, "file3.mp3");

        directoryMonitorService.verifyInputDirectory();

        Mockito.verify(fileReceivedMessageProducer, Mockito.never()).sendFileReceivedMessage(Mockito.any());

        removeDirectory(PROCESSED_DIRECTORY);
    }

    @Test
    void verifyInputDirectoryWithErrorWhenSendMessageToRabbit() throws IOException {
        mockRabbitProducerError("file4.dat");
        final String INPUT_DIRECTORY = TEMP_DIRECTORY + "/in3";
        final String PROCESSED_DIRECTORY = TEMP_DIRECTORY + "/pr3";
        directoryMonitorService = new DirectoryMonitorService(
                fileReceivedMessageProducer, INPUT_DIRECTORY, PROCESSED_DIRECTORY, ".dat");

        createTempFileInInputDirectory( INPUT_DIRECTORY, "file4.dat");

        directoryMonitorService.verifyInputDirectory();

        List<String> filenames = Files.list(Paths.get(INPUT_DIRECTORY))
                .map(Path::toFile)
                .map(File::getName)
                .collect(Collectors.toList());

        Assertions.assertTrue(filenames.get(0).endsWith("file4.dat"));

        removeDirectory(PROCESSED_DIRECTORY);
    }

    private void createTempFileInInputDirectory(String inputDirectory, String... filenames) throws IOException {
        for (String filename : filenames) {
            File file = new File(inputDirectory + File.separator + filename);
            file.createNewFile();
            file.deleteOnExit();
        }
    }

    private void mockRabbitProducerBehavior() {
        Mockito.doNothing()
                .when(fileReceivedMessageProducer).sendFileReceivedMessage(Mockito.any());
    }

    private void mockRabbitProducerError(String filename) {
        Mockito.doThrow(new RabbitSendException(filename, "exception"))
                .when(fileReceivedMessageProducer).sendFileReceivedMessage(Mockito.any());
    }

    private void removeDirectory(String... directories) throws IOException {
        for (String directory : directories) {
            FileSystemUtils.deleteRecursively(Paths.get(directory));
        }
    }
}
