package com.dev.phosell.session.domain.exception;

public class InvalidSessionTimeException extends RuntimeException {
    public InvalidSessionTimeException()
    {
        super("Invalid session time");
    }
}
