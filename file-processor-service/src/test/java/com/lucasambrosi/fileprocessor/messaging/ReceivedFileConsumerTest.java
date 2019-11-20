package com.lucasambrosi.fileprocessor.messaging;

import com.lucasambrosi.fileprocessor.dto.ReceivedFileMessageDto;
import com.lucasambrosi.fileprocessor.service.FileProcessorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
public class ReceivedFileConsumerTest {

    private static final String FILENAME = "nome-do-arquivo";

    @InjectMocks
    private ReceivedFileConsumer receivedFileConsumer;

    @Mock
    private FileProcessorService fileProcessorService;

    @Test
    void consumeMessageTest() {
        receivedFileConsumer.consume(buildReceivedFileMessageDto(FILENAME));

        ArgumentCaptor<ReceivedFileMessageDto> arg = ArgumentCaptor.forClass(ReceivedFileMessageDto.class);
        Mockito.verify(fileProcessorService, Mockito.only()).processFile(arg.capture());

        ReceivedFileMessageDto dto = arg.getValue();
        Assertions.assertEquals(FILENAME, dto.getFilename());
        Assertions.assertEquals(LocalDate.now(), dto.getReceivedDate().toLocalDate());
    }

    @Test
    void consumeMessageWithErrorTest() {
        mockFileProcessorError();
        Throwable throwable = Assertions.assertThrows(
                RuntimeException.class,
                () -> receivedFileConsumer.consume(buildReceivedFileMessageDto(FILENAME)));

        Assertions.assertEquals("Error processing file '" + FILENAME + "'.", throwable.getMessage());
    }

    private ReceivedFileMessageDto buildReceivedFileMessageDto(final String filename) {
        ReceivedFileMessageDto dto = new ReceivedFileMessageDto();
        dto.setFilename(filename);
        dto.setReceivedDate(LocalDateTime.now());
        return dto;
    }

    private void mockFileProcessorError() {
        Mockito.doThrow(new RuntimeException()).when(fileProcessorService).processFile(Mockito.any());
    }
}
