package com.dev.phosell.Authentication.domain.exception.refreshToken;

public class InvalidUserOrPasswordException extends RuntimeException{

    public InvalidUserOrPasswordException(){
        super("Invalid username or password");
    }

}
