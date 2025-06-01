package com.dev.phosell.session.domain.exception.session;

public class SessionOwnershipException extends RuntimeException {

    public SessionOwnershipException() {
        super("Your are not the owner of this session");
    }
}
