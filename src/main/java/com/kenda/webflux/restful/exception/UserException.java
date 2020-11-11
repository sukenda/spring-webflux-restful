package com.kenda.webflux.restful.exception;

public class UserException extends RuntimeException {

    private final String message;

    public UserException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
