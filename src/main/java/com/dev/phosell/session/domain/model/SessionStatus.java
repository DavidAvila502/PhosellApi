package com.dev.phosell.session.domain.model;

public enum SessionStatus {
    REQUESTED,
    CONFIRMED,
    IN_PROGRESS,
    PHOTOS_PENDING,
    COMPLETED,
    CANCELLED_BY_CLIENT,
    CANCELLED_BY_ADMIN
}
