package com.example.SpringBootMicroservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRequest {

    @NotBlank(message = "The name is required")
    private String name;
}
