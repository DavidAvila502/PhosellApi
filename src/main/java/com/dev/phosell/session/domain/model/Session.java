package com.dev.phosell.session.domain.model;

import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.user.domain.model.User;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;


public class Session {
    private UUID id;
    private User client;
    private User photographer;
    private SessionPackage sessionPackage;
    private LocalDate sessionDate;
    private LocalTime sessionTime;
    private String location;
    private SessionStatus sessionStatus;
    private String photosLink;
    private String cancelReason;

    public Session(
            UUID id,
            User client,
            User photographer,
            SessionPackage sessionPackage,
            LocalDate sessionDate,
            LocalTime sessionTime,
            String location,
            SessionStatus sessionStatus,
            String photosLink,
            String cancelReason
    ) {
        this.id = id;
        this.client = client;
        this.photographer = photographer;
        this.sessionPackage = sessionPackage;
        this.sessionDate = sessionDate;
        this.sessionTime = sessionTime;
        this.location = location;
        this.sessionStatus = sessionStatus;
        this.photosLink = photosLink;
        this.cancelReason = cancelReason;
    }

    public Session(){
    }

    // getter
    public UUID getId() {
        return id;
    }

    public User getClient() {
        return client;
    }

    public User getPhotographer() {
        return photographer;
    }

    public SessionPackage getSessionPackage() {
        return sessionPackage;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public LocalTime getSessionTime() {
        return sessionTime;
    }

    public String getLocation() {
        return location;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public String getPhotosLink() {
        return photosLink;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    // setter
    public void setId(UUID id) {
        this.id = id;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public void setPhotographer(User photographer) {
        this.photographer = photographer;
    }

    public void setSessionDate(LocalDate sessionDate) {
        this.sessionDate = sessionDate;
    }

    public void setSessionPackage(SessionPackage sessionPackage) {
        this.sessionPackage = sessionPackage;
    }

    public void setSessionTime(LocalTime sessionTime) {
        this.sessionTime = sessionTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public void setPhotosLink(String photosLink) {
        this.photosLink = photosLink;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }
}
