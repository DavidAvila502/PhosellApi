package com.dev.phosell.authentication.domain.exception.auth;

public class InvalidUserOrPasswordException extends RuntimeException{

    public InvalidUserOrPasswordException(){
        super("Invalid username or password");
    }

}
