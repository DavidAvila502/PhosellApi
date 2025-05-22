package com.dev.phosell.session.domain.exception.photographer;

public class NotAvailablePhotographerException extends RuntimeException {
    public NotAvailablePhotographerException() {
        super("There is not available photographer for this session");
    }
}
