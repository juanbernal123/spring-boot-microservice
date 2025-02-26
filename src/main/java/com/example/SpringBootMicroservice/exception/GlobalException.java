package com.example.SpringBootMicroservice.exception;

import com.example.SpringBootMicroservice.response.ApiResponse;
import com.example.SpringBootMicroservice.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalException {

    @Autowired
    private ResponseUtil responseUtil;
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ApiResponse<Object> apiResponse = responseUtil.singleError(ex.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND.value(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(UsernameTakenException.class)
    public ResponseEntity<ApiResponse<Object>> userAlreadyExistException(UsernameTakenException ex, HttpServletRequest request) {
        ApiResponse<Object> apiResponse = responseUtil.singleError(ex.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(UserRegistrationException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserRegistrationException(UserRegistrationException ex, HttpServletRequest request) {
        ApiResponse<Object> apiResponse = responseUtil.singleError(ex.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(EmailTakenException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailTakenException(EmailTakenException ex, HttpServletRequest request) {
        ApiResponse<Object> apiResponse = responseUtil.singleError(ex.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(RoleNameTakenException.class)
    public ResponseEntity<ApiResponse<Object>> handleRoleNameTakenException(RoleNameTakenException ex, HttpServletRequest request) {
        ApiResponse<Object> apiResponse = responseUtil.singleError(ex.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, List<String>> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.computeIfAbsent(error.getField(), key -> new ArrayList<>()).add(error.getDefaultMessage());
        });

        ApiResponse<Object> apiResponse = responseUtil.error(errors, HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationErrors(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String errorMessage = messageSource.getMessage("exception.body.required.message", null, request.getLocale());
        ApiResponse<Object> apiResponse = responseUtil.singleError(errorMessage, HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

}
