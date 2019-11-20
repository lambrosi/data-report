package com.lucasambrosi.fileprocessor.model;

import java.util.List;
import java.util.regex.Matcher;

public class Sale implements Model<Sale> {

    private static final int ID_INDEX = 2;
    private static final int ITEMS_INDEX = 3;
    private static final int SALESMAN_NAME_INDEX = 4;

    private Long id;
    private List<Item> items;
    private String salesmanName;

    @Override
    public Sale of(Matcher splittedSale) {
        this.id = Long.valueOf(splittedSale.group(ID_INDEX));
        this.items = Item.listOf(splittedSale.group(ITEMS_INDEX));
        this.salesmanName = splittedSale.group(SALESMAN_NAME_INDEX);
        return this;
    }

    public Long getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public Double getTotalPriceSale() {
        return items.stream()
                .mapToDouble(it -> it.getPrice() * it.getQuantity())
                .sum();
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", items=" + items +
                ", salesmanName='" + salesmanName + '\'' +
                '}';
    }
}
