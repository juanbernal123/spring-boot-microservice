package com.example.SpringBootMicroservice.exception;

public class DataIntegrityException extends Exception {
    public DataIntegrityException(String message) {
        super(message);
    }
}
