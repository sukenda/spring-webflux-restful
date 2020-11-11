package com.kenda.webflux.restful.exception;

public class DataException extends RuntimeException {

    private final String message;

    private final String httpStatus;

    public DataException(String message, String httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

}
