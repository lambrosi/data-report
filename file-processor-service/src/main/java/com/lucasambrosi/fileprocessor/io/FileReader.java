package com.lucasambrosi.fileprocessor.io;

import com.lucasambrosi.fileprocessor.exception.FileNoContentException;
import com.lucasambrosi.fileprocessor.dto.FileContentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

@Component
public class FileReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileReader.class);

    public FileContentDto<String> extractContent(final File file) {
        final String FILENAME = file.getName();
        try {
            LOGGER.info("Reading file: " + FILENAME);
            List<String> listLines = Files.readAllLines(file.toPath(), Charset.defaultCharset());

            if (!listLines.isEmpty()) {
                return new FileContentDto<>(getFilenameWithoutExtension(FILENAME), listLines);
            }
        } catch (IOException ex) {
            LOGGER.error("Error on extract the content of the file: " + file.getName());
            throw new RuntimeException(ex.getMessage());
        }
        throw new FileNoContentException("File " + FILENAME + " has no content.");
    }

    private String getFilenameWithoutExtension(final String filename) {
        int extensionIndex = filename.lastIndexOf(".");
        return filename.substring(0, extensionIndex);
    }
}
