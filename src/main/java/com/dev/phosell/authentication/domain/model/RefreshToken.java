package com.dev.phosell.authentication.domain.model;

import com.dev.phosell.user.domain.model.User;
import java.sql.Timestamp;
import java.util.UUID;


public class RefreshToken {
    private UUID id;
    private String token;
    private User user;
    private Timestamp expiryDate;


    public RefreshToken(UUID id, String token, Timestamp expiryDate, User user) {
        this.id = id;
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public RefreshToken() {
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }


    public UUID getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }

    public Timestamp getExpiryDate() {
        return expiryDate;
    }
}
