package com.example.SpringBootMicroservice.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "{notBlank.message}")
    @NotNull(message = "{notNull.message}")
    @Size(min = 4)
    private String username;

    @NotBlank(message = "{notBlank.message}")
    @NotNull(message = "{notNull.message}")
    @Size(min = 8)
    private String password;
}
