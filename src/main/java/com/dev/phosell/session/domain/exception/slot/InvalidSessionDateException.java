package com.dev.phosell.session.domain.exception.slot;

public class InvalidSessionDateException extends RuntimeException {
    private String value;
    private String description;

    public InvalidSessionDateException(String value, String description) {
        super(String.format("Invalid session date %s: %s",value,description));
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }
}
