package com.lucasambrosi.fileprocessor.model;

import com.lucasambrosi.fileprocessor.enums.DataType;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;

import static com.lucasambrosi.fileprocessor.enums.DataType.CUSTOMER;
import static com.lucasambrosi.fileprocessor.enums.DataType.SALE;
import static com.lucasambrosi.fileprocessor.enums.DataType.SALESMAN;

public class ModelFactory {

    private static final Map<DataType, Supplier<Model>> MODELS = new EnumMap<DataType, Supplier<Model>>(DataType.class) {{
        put(SALESMAN, Salesman::new);
        put(CUSTOMER, Customer::new);
        put(SALE, Sale::new);
    }};

    public static <T extends Model> T getModel(/*String[] splittedData*/Matcher matcher, DataType type) {
        return (T) MODELS.get(type).get().of(matcher);
    }
}
