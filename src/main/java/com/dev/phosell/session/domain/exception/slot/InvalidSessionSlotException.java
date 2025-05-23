package com.dev.phosell.session.domain.exception.slot;

public class InvalidSessionSlotException extends RuntimeException {
    private String value;
    private String description;

    public InvalidSessionSlotException(String value, String description) {
        super(String.format("Invalid session slot %s: %s",value,description));
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
