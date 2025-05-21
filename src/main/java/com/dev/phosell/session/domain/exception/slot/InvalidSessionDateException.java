package com.dev.phosell.session.domain.exception.slot;

public class InvalidSessionDateException extends RuntimeException {
    public InvalidSessionDateException() {
        super("Invalid session date");
    }
}
