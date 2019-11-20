package com.lucasambrosi.fileprocessor.report;

import com.lucasambrosi.fileprocessor.dto.FileContentDto;
import com.lucasambrosi.fileprocessor.dto.ReportDto;
import com.lucasambrosi.fileprocessor.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtil;

import java.util.Arrays;
import java.util.List;

public class ReportProcessorTest {

    private ReportProcessor reportProcessor = new ReportProcessor();

    @Test
    void generateReportTest() {
        List<String> fileLines = this.getFileLines();
        FileContentDto<String> fileContentDto = new FileContentDto<>("filename", fileLines);

        FileContentDto<Model> fileContentParsed = parseFileContent(fileContentDto);

        ReportDto reportDto = reportProcessor.generateReport(fileContentParsed);

        Assertions.assertEquals("filename", reportDto.getFilename());
        Assertions.assertEquals(2L, reportDto.getAmountCustomers());
        Assertions.assertEquals(2L, reportDto.getAmountSalesman());
        Assertions.assertEquals(10L, reportDto.getMostExpensiveSaleId());
        Assertions.assertEquals("Paulo", reportDto.getWorstSalesmanName());
        Assertions.assertTrue(reportDto.toString().contains("amountSalesman=2"));
    }

    private List<String> getFileLines() {
        return Arrays.asList(
                "001ç1234567891234çPedroç50000",
                "001ç3245678865434çPauloç40000.99",
                "002ç2345675434544345çJose da SilvaçRural",
                "002ç2345675433444345çEduardo PereiraçRural",
                "003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çPedro",
                "003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çPaulo"
        );
    }

    private FileContentDto<Model> parseFileContent(FileContentDto<String> rawData) {
        return TestUtil.getFileParser().processContent(rawData);
    }
}
