package com.example.SpringBootMicroservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleRequest {

    @NotBlank(message = "{notBlank.message}")
    @NotNull(message = "{notNull.message}")
    private String name;
}
