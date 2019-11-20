package com.lucasambrosi.fileprocessor.io;

import com.lucasambrosi.fileprocessor.dto.ReportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileWriter.class);

    private String outputDirectory;
    private String outputExtension;

    public FileWriter(@Value("${application.path.output-directory}") String outputDirectory,
                      @Value("${application.file-output-extension}") String outputExtension) {
        this.outputDirectory = outputDirectory;
        this.outputExtension = outputExtension;

        this.createOutputDirectory();
    }

    public ReportDto writeReport(ReportDto reportDto) {
        final String REPORT_FILENAME = generateFilenameWithExtension(reportDto.getFilename());
        final Path FULL_PATH = getFullPath(REPORT_FILENAME);

        LOGGER.info("Writing reportDto in '" + outputDirectory + "': " + REPORT_FILENAME);
        try (BufferedWriter writer = Files.newBufferedWriter(FULL_PATH)) {
            this.writeReportIntoFile(writer, reportDto);
            LOGGER.info(REPORT_FILENAME + " created successfully!");
        } catch (IOException ex) {
            LOGGER.error("Error on create reportDto '" + REPORT_FILENAME + "'.");
            throw new RuntimeException(ex.getMessage());
        }
        return reportDto;
    }

    private boolean createOutputDirectory() {
        LOGGER.info("Creating {} directory.", outputDirectory);
        return new File(outputDirectory).mkdirs();
    }

    private String generateFilenameWithExtension(String filename) {
        return filename + outputExtension;
    }

    private Path getFullPath(String filename) {
        return Paths.get(outputDirectory, filename);
    }

    private void writeReportIntoFile(BufferedWriter output, ReportDto reportDto) throws IOException {
        final String LINE_SEPARATOR = System.getProperty("line.separator");
        output.write("Amount clients: " + reportDto.getAmountCustomers() + LINE_SEPARATOR);
        output.write("Amount salesman: " + reportDto.getAmountSalesman() + LINE_SEPARATOR);
        output.write("Most expensive sale ID: " + reportDto.getMostExpensiveSaleId() + LINE_SEPARATOR);
        output.write("Worst salesman: " + reportDto.getWorstSalesmanName() + LINE_SEPARATOR);
    }
}
