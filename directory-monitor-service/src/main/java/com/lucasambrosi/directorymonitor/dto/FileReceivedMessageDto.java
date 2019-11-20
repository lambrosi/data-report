package com.lucasambrosi.directorymonitor.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FileReceivedMessageDto implements Serializable {

    private String filename;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime receivedDate;

    public FileReceivedMessageDto(String filename) {
        this.filename = filename;
        this.receivedDate = LocalDateTime.now();
    }

    public String getFilename() {
        return filename;
    }

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }
}
