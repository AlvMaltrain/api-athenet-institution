package com.athenet.institution.exception;

// Error al no encontrar buscando por ID
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}