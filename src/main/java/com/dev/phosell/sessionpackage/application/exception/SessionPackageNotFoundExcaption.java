package com.dev.phosell.sessionpackage.application.exception;

public class SessionPackageNotFoundExcaption extends RuntimeException{

    private String value;
    private String field;

    public SessionPackageNotFoundExcaption(String field, String value){
        super(String.format("SessionPackage not found with: %s: %s",field,value));
        this.field = field;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getField() {
        return field;
    }
}
