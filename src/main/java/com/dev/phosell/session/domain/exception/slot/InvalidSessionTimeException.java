package com.dev.phosell.session.domain.exception.slot;

public class InvalidSessionTimeException extends RuntimeException {
    public InvalidSessionTimeException()
    {
        super("Invalid session time");
    }
}
