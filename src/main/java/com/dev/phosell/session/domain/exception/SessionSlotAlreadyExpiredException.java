package com.dev.phosell.session.domain.exception;

public class SessionSlotAlreadyExpiredException extends RuntimeException {
    public SessionSlotAlreadyExpiredException() {
        super("Session slot is already expired");
    }
}
