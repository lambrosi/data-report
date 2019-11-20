package com.lucasambrosi.fileprocessor.enums;

import com.lucasambrosi.fileprocessor.exception.DataTypeNotAllowedException;

public enum DataType {

    SALESMAN("001"),
    CUSTOMER("002"),
    SALE("003");

    private String id;

    DataType(String id) {
        this.id = id;
    }

    public static DataType of(String id) {
        for (DataType dataType : DataType.values()) {
            if (dataType.getId().equals(id.trim())) {
                return dataType;
            }
        }
        throw new DataTypeNotAllowedException("ID '" + id + "' not allowed.");
    }

    public String getId() {
        return id;
    }
}
