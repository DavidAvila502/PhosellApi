package com.dev.phosell.session.domain.exception.session;

public class InvalidSessionValueException extends RuntimeException {
    public String field;
    public String value;

    public InvalidSessionValueException(String field, String value) {
        super(String.format("Invalid value at %s: %s",field,value));
    }

    public InvalidSessionValueException(String field, String value, String description) {
        super(String.format("Invalid value at %s: %s -%s",field,value,description));
    }
}
