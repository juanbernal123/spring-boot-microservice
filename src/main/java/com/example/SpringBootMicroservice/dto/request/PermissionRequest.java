package com.example.SpringBootMicroservice.dto.request;

import com.example.SpringBootMicroservice.entity.Permission;
import com.example.SpringBootMicroservice.validation.UniqueColumn;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionRequest {
    @NotEmpty
    @NotNull
    @UniqueColumn(field = "name", entity = Permission.class)
    private String name;
    private String display_name;
}
