package com.dev.phosell.session.domain.exception.session;

public class SessionStatusNotFound extends RuntimeException {
    private String value;

    public SessionStatusNotFound(String value) {
        super(String.format("The status %s doesn't exist",value));
    }

    public String getValue() {
        return value;
    }
}
