package com.example.SpringBootMicroservice.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private Object errors;
    private int status_code;
    private LocalDateTime timestamp;
    private String path;
}
