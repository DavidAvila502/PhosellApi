package com.dev.phosell.session.domain.exception;

public class SessionSlotOutOfWorkingHourException extends RuntimeException {
    public SessionSlotOutOfWorkingHourException() {
        super("You can't request a session for this hour");
    }
}
