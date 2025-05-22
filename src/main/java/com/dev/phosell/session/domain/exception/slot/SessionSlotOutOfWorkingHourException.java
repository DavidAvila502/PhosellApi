package com.dev.phosell.session.domain.exception.slot;

public class SessionSlotOutOfWorkingHourException extends RuntimeException {
    public SessionSlotOutOfWorkingHourException() {
        super("You can't request a session for this hour");
    }
}
