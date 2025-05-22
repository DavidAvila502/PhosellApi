package com.dev.phosell.authentication.infrastructure.exception;

public class InvalidUserOrPasswordException extends RuntimeException{

    public InvalidUserOrPasswordException(){
        super("Invalid username or password");
    }

}
