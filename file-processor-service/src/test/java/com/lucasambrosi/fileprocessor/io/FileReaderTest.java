package com.lucasambrosi.fileprocessor.io;

import com.lucasambrosi.fileprocessor.dto.FileContentDto;
import com.lucasambrosi.fileprocessor.exception.FileNoContentException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileReaderTest {

    private FileReader fileReader = new FileReader();

    @Test
    void readNotFoundFileTest() {
        Assertions.assertThrows(
                RuntimeException.class,
                () -> fileReader.extractContent(new File("tmp/teste.t")));
    }

    @Test
    void readEmptyFileTest() throws IOException {
        File tempFile = getTempFile("data-analysis-test");
        tempFile.deleteOnExit();

        Assertions.assertThrows(
                FileNoContentException.class,
                () -> fileReader.extractContent(tempFile));
    }

    @Test
    void readFileWithContentTest() throws IOException {
        List<String> content = new ArrayList<String>() {{
            add("LINE--1\n");
            add("LINE--2");
        }};

        File tempFile = getTempFileWithContent("data-analysis-with-content", content);
        tempFile.deleteOnExit();

        FileContentDto<String> fileContet = fileReader.extractContent(tempFile);
        Assertions.assertTrue(fileContet.getFilename().contains("data-analysis-with-content"));
        Assertions.assertEquals(content.size(), fileContet.getContent().size());
    }

    private File getTempFile(String filename) throws IOException {
        return File.createTempFile(filename, null);
    }

    private File getTempFileWithContent(String filename, List<String> content) throws IOException {
        File tempFile = getTempFile(filename);

        BufferedWriter writer = Files.newBufferedWriter(tempFile.toPath());
        for (String line : content) {
            writer.write(line);
        }
        writer.close();

        return tempFile;
    }
}
