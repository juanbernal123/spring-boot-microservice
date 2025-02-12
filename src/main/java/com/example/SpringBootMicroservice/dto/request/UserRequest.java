package com.example.SpringBootMicroservice.dto.request;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
}
