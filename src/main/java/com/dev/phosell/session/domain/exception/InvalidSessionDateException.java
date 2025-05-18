package com.dev.phosell.session.domain.exception;

public class InvalidSessionDateException extends RuntimeException {
    public InvalidSessionDateException() {
        super("Invalid session date");
    }
}
