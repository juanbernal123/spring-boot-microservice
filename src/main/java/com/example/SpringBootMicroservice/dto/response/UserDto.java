package com.example.SpringBootMicroservice.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String name;
    private LocalDateTime created_at; // TODO: cambiar a string
    private LocalDateTime updated_at; // TODO: cambiar a string
}
