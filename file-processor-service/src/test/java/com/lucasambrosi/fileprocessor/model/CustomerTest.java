package com.lucasambrosi.fileprocessor.model;

import com.lucasambrosi.fileprocessor.enums.DataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerTest {

    private Pattern customerPattern = Pattern.compile(TestUtil.CUSTOMER_REGEX);

    @Test
    void processCustomerTest() {
        final String CUSTOMER_LINE = "002ç2345675434544345çJose da SilvaçRural";
        Matcher matcher = customerPattern.matcher(CUSTOMER_LINE);
        Assertions.assertTrue(matcher.find());

        Customer customer = ModelFactory.getModel(matcher, DataType.CUSTOMER);

        Assertions.assertNotNull(customer);
        Assertions.assertEquals(2345675434544345L, customer.getCnpj());
        Assertions.assertEquals("Jose da Silva", customer.getName());
        Assertions.assertEquals("Rural", customer.getBusinessArea());

        Assertions.assertTrue(customer.toString().contains("Jose da Silva"));
        Assertions.assertTrue(customer.toString().contains("Rural"));
    }
}
