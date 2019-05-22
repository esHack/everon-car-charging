package com.everon.charger.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class CarChargingExceptionHandler {

    @ExceptionHandler(CarChargeException.class)
    public ResponseEntity<String> handleRunTimeException(CarChargeException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
