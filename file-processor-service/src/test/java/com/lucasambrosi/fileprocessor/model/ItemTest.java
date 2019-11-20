package com.lucasambrosi.fileprocessor.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ItemTest {

    @Test
    void parseItemTest() {
        final String ITEM_LINE = "[1-10-100,2-30-2.50]";

        List<Item> itemList = Item.listOf(ITEM_LINE);
        Assertions.assertNotNull(itemList);
        Assertions.assertEquals(2, itemList.size());

        Item itemOne = itemList.get(0);
        Assertions.assertEquals(Long.valueOf(1L), itemOne.getId());
        Assertions.assertEquals(Integer.valueOf(10), itemOne.getQuantity());
        Assertions.assertEquals(100, itemOne.getPrice(), .1);

        Item itemTwo = itemList.get(1);
        Assertions.assertEquals(Long.valueOf(2L), itemTwo.getId());
        Assertions.assertEquals(Integer.valueOf(30), itemTwo.getQuantity());
        Assertions.assertEquals(2.50, itemTwo.getPrice(), .1);

        Assertions.assertTrue(itemTwo.toString().contains("quantity=30"));
    }
}
