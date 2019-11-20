package com.lucasambrosi.fileprocessor.parser;

import com.lucasambrosi.fileprocessor.enums.DataType;
import com.lucasambrosi.fileprocessor.exception.LineNotConvertibleException;
import com.lucasambrosi.fileprocessor.dto.FileContentDto;
import com.lucasambrosi.fileprocessor.model.Model;
import com.lucasambrosi.fileprocessor.model.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class FileParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileParser.class);
    private Map<DataType, Pattern> parsePattern;

    public FileParser(@Value("${application.parsing.regex.salesman}") String salesmanRegex,
                      @Value("${application.parsing.regex.customer}") String customerRegex,
                      @Value("${application.parsing.regex.sale}") String saleRegex) {
        parsePattern = new EnumMap<>(DataType.class);
        parsePattern.put(DataType.SALESMAN, Pattern.compile(salesmanRegex));
        parsePattern.put(DataType.CUSTOMER, Pattern.compile(customerRegex));
        parsePattern.put(DataType.SALE, Pattern.compile(saleRegex));
    }

    public FileContentDto<Model> processContent(FileContentDto<String> fileContentDto) {
        LOGGER.info("Processing file: " + fileContentDto.getFilename());
        List<Model> lineProcessed = processLines(fileContentDto.getContent());
        return new FileContentDto<>(fileContentDto.getFilename(), lineProcessed);
    }

    private List<Model> processLines(List<String> lines) {
        return lines.stream()
                .map(this::parseLine)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Model parseLine(String line) {
        DataType type = DataType.of(getRawDataType(line));
        Matcher matcher = parsePattern.get(type).matcher(line);
        if (matcher.find()) {
            return ModelFactory.getModel(matcher, type);
        }
        throw new LineNotConvertibleException("Line '" + line + "' not convertible!");
    }

    private String getRawDataType(String rawData) {
        return rawData.substring(0, 3);
    }
}
