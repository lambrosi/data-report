package com.lucasambrosi.fileprocessor.model;

import java.util.regex.Matcher;

public interface Model<T extends Model> {

    T of(/*String[]*/Matcher splittedData);
}
