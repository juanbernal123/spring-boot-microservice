package com.example.SpringBootMicroservice.util;

import com.example.SpringBootMicroservice.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;

@Component
@AllArgsConstructor
public class ResponseUtil {

    private final MessageSource messageSource;

    // si no tiene args el mensaje
    public <T> ResponseEntity<T> success(T data, HttpServletRequest request, String message, HttpStatus httpStatus) {
        return success(data, request, httpStatus, message, null);
    }

    // si no tiene ningun mensaje
    public <T> ResponseEntity<T> success(T data, HttpServletRequest request) {
        return success(data, request, HttpStatus.OK, null, null);
    }

    // metodo completo
    public <T> ResponseEntity<T> success(T data, HttpServletRequest request, HttpStatus httpStatus, String messageCode, Object... messageParams) {
        String messageResponse = (messageCode != null && !messageCode.isEmpty()) ? getMessage(messageCode, request, messageParams) : "";
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(Boolean.TRUE);
        apiResponse.setMessage(messageResponse);
        apiResponse.setData(data);
        apiResponse.setErrors(null);
        apiResponse.setStatus_code(httpStatus.value());
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setPath(request.getRequestURI());
        return ResponseEntity.status(httpStatus).body((T) apiResponse);
    }

    public <T> ApiResponse<T> error(Object errors, String message, int errorCode, String path) {
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setSuccess(Boolean.FALSE);
        apiResponse.setMessage(message);
        apiResponse.setData(null);
        apiResponse.setErrors(errors);
        apiResponse.setStatus_code(errorCode);
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setPath(path);
        return apiResponse;
    }

    public <T> ApiResponse<T> singleError(String error, String message, int errorCode, String path) {
        return error(Collections.singletonList(error), message, errorCode, path);
    }

    public String getMessage(String code, HttpServletRequest request, Object... args) {
        return messageSource.getMessage(code, args, request.getLocale());
    }
}
