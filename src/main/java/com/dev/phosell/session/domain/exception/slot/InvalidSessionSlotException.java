package com.dev.phosell.session.domain.exception.slot;

public class InvalidSessionSlotException extends RuntimeException {
    public InvalidSessionSlotException() {
        super("Invalid session slot");
    }
}
