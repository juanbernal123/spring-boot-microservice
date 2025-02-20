package com.example.SpringBootMicroservice.exception;

import java.util.List;

public class UserRegistrationException extends RuntimeException {
    private final List<String> errors;

    public UserRegistrationException(List<String> errors) {
        super("Error en el registro");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}