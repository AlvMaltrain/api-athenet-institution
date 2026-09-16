package com.athenet.institution.exception;

//Error para duplicados
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}