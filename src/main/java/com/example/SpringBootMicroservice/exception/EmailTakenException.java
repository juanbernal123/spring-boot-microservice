package com.example.SpringBootMicroservice.exception;

public class EmailTakenException extends Exception {
    public EmailTakenException(String message) {
        super(message);
    }
}
