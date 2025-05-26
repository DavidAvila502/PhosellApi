package com.dev.phosell.session.domain.exception.session;

public class CancelSessionException extends RuntimeException {
    private String role;
    public CancelSessionException(String role)
    {
        super(String.format("A user with role: %s can't cancel a session",role));
    }
}
