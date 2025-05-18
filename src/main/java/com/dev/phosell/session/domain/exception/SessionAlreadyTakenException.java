package com.dev.phosell.session.domain.exception;

public class SessionAlreadyTakenException extends  RuntimeException{

    public SessionAlreadyTakenException(){
        super("The selected session is taken");
    }
}
