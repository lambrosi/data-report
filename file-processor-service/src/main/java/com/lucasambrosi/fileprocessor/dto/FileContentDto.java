package com.lucasambrosi.fileprocessor.dto;

import java.util.List;

public class FileContentDto<T> {

    private String filename;
    private List<T> content;

    public FileContentDto(String filename, List<T> content) {
        this.filename = filename;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public List<T> getContent() {
        return content;
    }
}
