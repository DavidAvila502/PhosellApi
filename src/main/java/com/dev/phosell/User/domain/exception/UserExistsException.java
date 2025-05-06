package com.dev.phosell.User.domain.exception;

public class UserExistsException extends  RuntimeException{

    private final String email;

    public UserExistsException(String email){
        super(String.format("A user already exist with the email: %s",email));
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
