package com.example.SpringBootMicroservice.exception;

public class RoleNameTakenException extends Exception {
    public RoleNameTakenException(String message) {
        super(message);
    }
}
