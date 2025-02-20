package com.example.SpringBootMicroservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "{notBlank.message}")
    @NotNull(message = "{notNull.message}")
    private String username;

    @NotBlank(message = "{notBlank.message}")
    @NotNull(message = "{notNull.message}")
    private String password;

    @NotBlank(message = "{notBlank.message}")
    @NotNull(message = "{notNull.message}")
    @Email(message = "{email.message}")
    private String email;
}
