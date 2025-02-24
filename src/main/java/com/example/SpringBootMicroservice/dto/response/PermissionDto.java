package com.example.SpringBootMicroservice.dto.response;

import lombok.Data;

@Data
public class PermissionDto {
    private Long id;
    private String name;
    private String display_name;
    private String created_at;
    private String updated_at;
}
