package com.dev.phosell.session.domain.model;

import com.dev.phosell.session.domain.exception.session.SessionStatusNotFound;

public enum SessionStatus {
    REQUESTED,
    CONFIRMED,
    IN_PROGRESS,
    PHOTOS_PENDING,
    COMPLETED,
    CANCELLED_BY_CLIENT,
    CANCELLED_BY_ADMIN;

    public static SessionStatus fromString(String status) {
        try {
            return valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new SessionStatusNotFound(status);
        }
    }
}
