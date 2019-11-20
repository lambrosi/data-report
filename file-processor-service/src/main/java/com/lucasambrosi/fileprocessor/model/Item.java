package com.lucasambrosi.fileprocessor.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Item {

    private static final String FIELD_DELIMITER = "-";
    private static final String ITEM_DELIMITER = ",";

    private Long id;
    private Integer quantity;
    private Double price;

    public Item(Long id, Integer quantity, Double price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

    public static Item of(String[] item) {
        return new Item(
                Long.valueOf(item[0]),
                Integer.valueOf(item[1]),
                Double.valueOf(item[2])
        );
    }

    public static List<Item> listOf(String rawItemList) {
        String items = rawItemList.replaceAll("(\\[)|(\\])", "");
        String[] list = items.split(ITEM_DELIMITER);

        return Arrays.stream(list)
                .map(it -> of(it.split(FIELD_DELIMITER)))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
