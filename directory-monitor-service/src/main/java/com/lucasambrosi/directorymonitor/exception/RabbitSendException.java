package com.lucasambrosi.directorymonitor.exception;

public class RabbitSendException extends RuntimeException {

    private String filename;

    public RabbitSendException(String filename, String exceptionMessage) {
        super(exceptionMessage);
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
