package com.ebanx.accountapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomerExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Integer> NoSuchElementHandler() {
        return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
    }
}