package com.telerikacademy.web.photocontest.exceptions;

public class UnauthorizedOperationException extends RuntimeException{
    public UnauthorizedOperationException() {
    }

    public UnauthorizedOperationException(String message) {
        super(message);
    }

}
