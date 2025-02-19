package com.example.SpringBootMicroservice.exception;

public class RoleNameTakenException extends RuntimeException {
    public RoleNameTakenException(String message) {
        super(message);
    }
}
