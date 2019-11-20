package com.lucasambrosi.fileprocessor.model;

import com.lucasambrosi.fileprocessor.enums.DataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SalesmanTest {

    private Pattern salesmanPattern = Pattern.compile(TestUtil.SALESMAN_REGEX);

    @Test
    void processSalesmanTest() {
        final String SALESMAN_LINE = "001ç1234567891234çPedroç50000";
        Matcher matcher = salesmanPattern.matcher(SALESMAN_LINE);
        Assertions.assertTrue(matcher.find());

        Salesman salesman = ModelFactory.getModel(matcher, DataType.SALESMAN);
        Assertions.assertNotNull(salesman);

        Assertions.assertEquals(Long.valueOf(1234567891234L), salesman.getCpf());
        Assertions.assertEquals("Pedro", salesman.getName());
        Assertions.assertEquals(50000D, salesman.getSalary(), .1);

        Assertions.assertTrue(salesman.toString().contains("Pedro"));
    }
}
