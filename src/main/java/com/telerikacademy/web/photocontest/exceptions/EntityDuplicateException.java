package com.telerikacademy.web.photocontest.exceptions;

public class EntityDuplicateException extends RuntimeException{
    private String errorType;

    public EntityDuplicateException() {
    }

    public EntityDuplicateException(String type, String attribute, String value) {
        super(String.format("%s with %s %s already exists!", type, attribute, value));
        this.errorType = attribute;
    }

    public String getErrorType() {
        return errorType;
    }

}
