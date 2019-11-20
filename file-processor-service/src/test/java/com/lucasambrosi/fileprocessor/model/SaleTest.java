package com.lucasambrosi.fileprocessor.model;

import com.lucasambrosi.fileprocessor.enums.DataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import util.TestUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaleTest {

    private Pattern salePattern = Pattern.compile(TestUtil.SALE_REGEX);

    @Test
    void processSaleTest() {
        final String SALE_LINE = "003ç10ç[1-1-100,2-3-50]çPedro";
        Matcher matcher = salePattern.matcher(SALE_LINE);
        Assertions.assertTrue(matcher.find());

        Sale sale = ModelFactory.getModel(matcher, DataType.SALE);

        Assertions.assertNotNull(sale);
        Assertions.assertEquals(Long.valueOf(10), sale.getId());
        Assertions.assertEquals("Pedro", sale.getSalesmanName());

        List<Item> items = sale.getItems();
        Assertions.assertEquals(2, items.size());
        Assertions.assertEquals(250, sale.getTotalPriceSale(), .1);
        Assertions.assertEquals(Long.valueOf(1L), items.get(0).getId());
        Assertions.assertEquals(Integer.valueOf(1), items.get(0).getQuantity());
        Assertions.assertEquals(100, items.get(0).getPrice(), .1);

        Assertions.assertTrue(sale.toString().contains("Pedro"));

    }
}
