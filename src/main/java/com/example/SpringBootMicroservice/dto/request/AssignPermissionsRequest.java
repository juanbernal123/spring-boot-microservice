package com.example.SpringBootMicroservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.context.MessageSource;

import java.util.List;

@Data
public class AssignPermissionsRequest {
    private final MessageSource messageSource;

    @NotNull(message = "{validation.permissions.notNull.message}")
    @Size()
    private List<Long> permissions;

}
