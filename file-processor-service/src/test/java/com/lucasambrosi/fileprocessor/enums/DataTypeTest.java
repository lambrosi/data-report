package com.lucasambrosi.fileprocessor.enums;

import com.lucasambrosi.fileprocessor.exception.DataTypeNotAllowedException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataTypeTest {

    @Test
    void validDatatypeTest() {
        final String stringType = "001";
        final DataType type = DataType.of(stringType);

        Assertions.assertEquals(DataType.SALESMAN, type);
    }

    @Test
    void invalidDatatypeTest() {
        final String stringType = "A53";

        Assertions.assertThrows(
                DataTypeNotAllowedException.class,
                () -> DataType.of(stringType)
        );
    }
}
