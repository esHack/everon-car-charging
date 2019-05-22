package com.everon.charger.exception;

import org.springframework.http.HttpStatus;

public class CarChargeException extends RuntimeException{

    private String message;
    private HttpStatus httpStatus;

    public CarChargeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public CarChargeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus=httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
