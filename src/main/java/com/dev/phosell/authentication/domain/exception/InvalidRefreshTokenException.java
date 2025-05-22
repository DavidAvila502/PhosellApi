package com.dev.phosell.authentication.domain.exception;

public class InvalidRefreshTokenException extends  RuntimeException{

    public InvalidRefreshTokenException(){
        super("Invalid refreshToken");
    }
}
