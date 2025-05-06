package com.dev.phosell.authentication.domain.exception.token;

public class InvalidRefreshTokenException extends  RuntimeException{

    public InvalidRefreshTokenException(){
        super("Invalid refreshToken");
    }
}
