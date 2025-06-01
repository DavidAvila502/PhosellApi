package com.dev.phosell.session.domain.model;

import com.dev.phosell.session.domain.exception.session.PermissionsSessionException;
import com.dev.phosell.session.domain.exception.session.InvalidSessionValueException;
import com.dev.phosell.sessionpackage.domain.model.SessionPackage;
import com.dev.phosell.user.domain.model.Role;
import com.dev.phosell.user.domain.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime cancelledAt;

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
            String cancelReason,
            LocalDateTime cancelledAt
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
        this.cancelledAt = cancelledAt;
    }

    public Session()
    {
        this.id = UUID.randomUUID();
    }

    public void complete(User user, String photosLink)
    {
        if(photosLink == null || photosLink.isEmpty())
        {
            throw new InvalidSessionValueException("photosLink", photosLink);
        }

        if(user.getRole() != Role.ADMIN && user.getRole() != Role.PHOTOGRAPHER){
            throw new PermissionsSessionException(user.getRole().toString(), "complete");
        }

        this.photosLink = photosLink;
        this.sessionStatus = SessionStatus.COMPLETED;

    }


    public void cancel(User user, String cancelReason)
    {
             if(user.getRole() != Role.ADMIN && user.getRole() != Role.CLIENT){
                 throw new PermissionsSessionException(user.getRole().toString(),"Cancel");
             }

             if(user.getRole() == Role.ADMIN){
                 this.sessionStatus = SessionStatus.CANCELLED_BY_ADMIN;

             }

             if(user.getRole() == Role.CLIENT){
                 this.sessionStatus = SessionStatus.CANCELLED_BY_CLIENT;
             }

             this.cancelledAt = LocalDateTime.now();
             this.cancelReason = cancelReason;
    }

    // validations
    public void validate(){
        validateId();
        validateClient();
        validatePhotographer();
        validateSessionPackage();
        validateSessionDate();
        validateSessionTime();
        validateLocation();
        validateSessionStatus();
    }


    private void validateId(){
        if(this.id == null){
            throw new InvalidSessionValueException("id",null);
        }
    }
    private void validateClient(){
        if(this.client == null){
            throw new InvalidSessionValueException("client",null);
        }
    }
    private void validatePhotographer(){
        if(this.photographer == null){
            throw new InvalidSessionValueException("photographer",null);
        }

        if(this.photographer.getRole() != Role.PHOTOGRAPHER){
            throw new InvalidSessionValueException("photographer.role",photographer.getRole().toString(),"the photographer has not the photographer role");
        }
    }
    private void validateSessionPackage(){
        if(this.sessionPackage == null){
            throw new InvalidSessionValueException("sessionPackage",null);
        }
    }
    private void validateSessionDate(){
        if(this.sessionDate == null){
            throw new InvalidSessionValueException("sessionDate",null);
        }
    }
    private void validateSessionTime(){
        if(this.sessionTime == null){
            throw new InvalidSessionValueException("sessionTime",null);
        }
    }
    private void validateLocation(){
        if(this.location == null){
            throw new InvalidSessionValueException("location",null);
        }
        if(this.location.length() <= 15){
            throw new InvalidSessionValueException("location",this.location,"the location must be at least 15 characters");

        }
    }
    private void validateSessionStatus(){
        if(this.sessionStatus == null){
            throw new InvalidSessionValueException("sessionStatus",null);
        }
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

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
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

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
}
