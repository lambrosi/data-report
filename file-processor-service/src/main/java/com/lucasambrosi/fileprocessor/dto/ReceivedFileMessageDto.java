package com.lucasambrosi.fileprocessor.dto;

import java.time.LocalDateTime;

public class ReceivedFileMessageDto {

    private String filename;
    private LocalDateTime receivedDate;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public LocalDateTime getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(LocalDateTime receivedDate) {
        this.receivedDate = receivedDate;
    }

}
