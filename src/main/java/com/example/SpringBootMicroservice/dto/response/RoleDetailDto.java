package com.example.SpringBootMicroservice.dto.response;

import lombok.Data;

import java.util.Set;

@Data
public class RoleDetailDto {
    private Long id;
    private String name;
    private String created_at;
    private String updated_at;
    private Set<PermissionDto> permissions;
}
