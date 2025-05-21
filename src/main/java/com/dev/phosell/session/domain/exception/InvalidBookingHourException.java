package com.dev.phosell.session.domain.exception;

public class InvalidBookingHourException extends RuntimeException {
    public InvalidBookingHourException() {
        super("You are trying to booking out of booking hour");
    }
}
