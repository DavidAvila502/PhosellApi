package com.dev.phosell.Authentication.domain.exception.refreshToken;

public class InvalidRefreshTokenException extends  RuntimeException{

    public InvalidRefreshTokenException(){
        super("Invalid refreshToken");
    }
}
