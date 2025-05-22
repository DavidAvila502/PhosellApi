package com.dev.phosell.user.domain.exception;

public class InvalidUserValueException extends RuntimeException {
    public String field;
    public String value;
    public String description;

    public InvalidUserValueException(String field, String value)
    {
        super(String.format("Invalid user value at %s: %s",field,value));
        this.field = field;
        this.value = value;
    }

    public InvalidUserValueException(String field, String value, String description)
    {
      super(String.format("Invalid user value at %s: %s -%s",field,value,description));
    }
}
