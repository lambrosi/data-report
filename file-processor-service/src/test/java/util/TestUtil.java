package util;

import com.lucasambrosi.fileprocessor.parser.FileParser;

import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public static final String SALESMAN_REGEX = "^([0-9]{3})ç([0-9]{10,15})ç([a-zA-Zç ]+)ç([0-9.,]+)$";
    public static final String CUSTOMER_REGEX = "^([0-9]{3})ç([0-9]{12,16})ç([a-zA-Z\\s]+)ç(.*)$";
    public static final String SALE_REGEX = "^([0-9]{3})ç([0-9]+)ç([^ç]+)ç([a-zA-Zç ]+)$";

    public static FileParser getFileParser() {
        return new FileParser(SALESMAN_REGEX, CUSTOMER_REGEX, SALE_REGEX);
    }
}
