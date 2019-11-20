package com.lucasambrosi.directorymonitor.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileSystemUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSystemUtil.class);

    public static void createDirectoriesInUserHome(String... directories) {
        for(String directory : directories) {
            createDirectory(directory);
        }
    }

    private static void createDirectory(final String directory) {
        File dir = new File(directory);
        if (!dir.exists() && !dir.mkdirs()) {
            LOGGER.error("Error on creating directory.");
            throw new RuntimeException("Could not create directory " + directory);
        }
        LOGGER.info("Directory '" + directory + "' exists/created!");
    }

    public static File moveFileTo(File file, String destinationDir) {
        try {
            return Files.move(file.toPath(), Paths.get(destinationDir + File.separator + file.getName())).toFile();
        } catch (IOException ex) {
            LOGGER.info("Error on moving file '{}' to folder '{}'.", file.getName(), destinationDir);
            throw new RuntimeException(ex);
        }
    }
}
