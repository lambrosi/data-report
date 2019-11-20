package com.lucasambrosi.fileprocessor.parser;

import com.lucasambrosi.fileprocessor.dto.FileContentDto;
import com.lucasambrosi.fileprocessor.exception.LineNotConvertibleException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.TestUtil;

import java.util.Collections;
import java.util.List;

public class FileParserTest {

    private static FileParser fileParser;

    @BeforeAll
    static void init() {
        fileParser = new FileParser(
                TestUtil.SALESMAN_REGEX,TestUtil.CUSTOMER_REGEX, TestUtil.SALE_REGEX);
    }

    @Test
    void processContentWithLineThatCannotBeParsed() {
        List<String> contentList = Collections.singletonList("001çASADFDDGFGç000065");
        FileContentDto<String> contentDto = new FileContentDto<>("filename", contentList);

        Throwable throwable = Assertions.assertThrows(LineNotConvertibleException.class,
                () -> fileParser.processContent(contentDto));

        Assertions.assertEquals("Line '" + contentList.get(0) + "' not convertible!", throwable.getMessage());
    }
}
