package com.dev.phosell.session.domain.exception;

public class InvalidSessionSlotException extends RuntimeException {
    public InvalidSessionSlotException() {
        super("Invalid session slot");
    }
}
