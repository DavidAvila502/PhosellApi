package com.dev.phosell.session.application.exception;

public class SessionAlreadyTakenException extends  RuntimeException{

    public SessionAlreadyTakenException(){
        super("The selected session is taken");
    }
}
