package com.example.SpringBootMicroservice.dto.response;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String created_at;
    private String updated_at;
}
