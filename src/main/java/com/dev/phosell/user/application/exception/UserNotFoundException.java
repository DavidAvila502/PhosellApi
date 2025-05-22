package com.dev.phosell.user.application.exception;

public class UserNotFoundException extends RuntimeException {
    private final String field;
    private final String value;

    public UserNotFoundException(String field, String value) {
        super(String.format("User not found with %s: %s", field, value));
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }
}