package com.lucasambrosi.fileprocessor.model;

import java.util.regex.Matcher;

public class Customer implements Model<Customer> {

    private static final int CNPJ_INDEX = 2;
    private static final int NAME_INDEX = 3;
    private static final int BUSINESS_AREA_INDEX = 4;

    private Long cnpj;
    private String name;
    private String businessArea;

    @Override
    public Customer of(Matcher splittedCustomer) {
        this.cnpj = Long.valueOf(splittedCustomer.group(CNPJ_INDEX));
        this.name = splittedCustomer.group(NAME_INDEX);
        this.businessArea = splittedCustomer.group(BUSINESS_AREA_INDEX);
        return this;
    }

    public Long getCnpj() {
        return cnpj;
    }

    public String getName() {
        return name;
    }

    public String getBusinessArea() {
        return businessArea;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cnpj=" + cnpj +
                ", name='" + name + '\'' +
                ", businessArea='" + businessArea + '\'' +
                '}';
    }
}
