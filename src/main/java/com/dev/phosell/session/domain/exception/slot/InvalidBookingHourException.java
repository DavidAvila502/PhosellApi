package com.dev.phosell.session.domain.exception.slot;

public class InvalidBookingHourException extends RuntimeException {
    public InvalidBookingHourException() {
        super("You are trying to booking out of booking hour");
    }
}
