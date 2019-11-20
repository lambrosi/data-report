package com.lucasambrosi.fileprocessor.io;

import com.lucasambrosi.fileprocessor.dto.ReportDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FileWriterTest {

    private FileWriter fileWriter;

    @Test
    void writeReportTest() {
        fileWriter = new FileWriter("/tmp", ".done.dat");

        ReportDto dto = buildReportDto();
        fileWriter.writeReport(dto);
    }

    @Test
    void writeReportInExistingDirectory() {
        fileWriter = new FileWriter("/root", ".done.dat");

        ReportDto dto = buildReportDto();
        Assertions.assertThrows(
                RuntimeException.class,
                () -> fileWriter.writeReport(dto));
    }

    private ReportDto buildReportDto() {
        return new ReportDto("filename", 5L, 7L, 8L, "Salesman");
    }
}
