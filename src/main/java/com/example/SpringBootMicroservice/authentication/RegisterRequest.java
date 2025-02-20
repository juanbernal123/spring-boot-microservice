package com.example.SpringBootMicroservice.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "{notBlank.message}")
    @NotNull(message = "{notNull.message}")
    @Size(min = 8, max = 100)
    private String username;

    @NotBlank(message = "{notBlank.message}")
    @NotNull(message = "{notNull.message}")
    @Size(min = 8, max = 100)
    private String password;

    @NotBlank(message = "{notBlank.message}")
    @NotNull(message = "{notNull.message}")
    @Email(message = "{email.message}")
    @UniqueElements
    private String email;
}
