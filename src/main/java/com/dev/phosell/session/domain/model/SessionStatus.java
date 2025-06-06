package com.dev.phosell.session.domain.model;

import com.dev.phosell.session.domain.exception.session.SessionStatusNotFound;
import java.util.List;

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

    public static List<SessionStatus> fromListString(List<String> statuses){
        return statuses.stream().map(s -> fromString(s)).toList();
    }

    public static List<String> toListString(List<SessionStatus> statuses){
        return statuses.stream().map(sessionStatus -> sessionStatus.toString()).toList();
    }

    public static List<SessionStatus> getBusyStatuses(){
        return List.of(REQUESTED,CONFIRMED,IN_PROGRESS,PHOTOS_PENDING);
    }

    public static List<SessionStatus> getFreeStatuses(){
        return List.of(COMPLETED,CANCELLED_BY_CLIENT,CANCELLED_BY_ADMIN);
    }
}
