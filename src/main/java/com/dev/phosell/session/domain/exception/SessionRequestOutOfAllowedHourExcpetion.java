package com.dev.phosell.session.domain.exception;

public class SessionRequestOutOfAllowedHourExcpetion extends RuntimeException {
    public SessionRequestOutOfAllowedHourExcpetion() {
        super("You are trying to request a session out of the allowed hour for today");
    }
}
