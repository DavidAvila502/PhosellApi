package com.dev.phosell.session.infrastructure.exception;

public class SessionNotFoundException extends RuntimeException {
    private final String field;
    private final String value;

    public SessionNotFoundException(String field, String value) {
        super(String.format("Session not found by %s: %s", field, value));
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
