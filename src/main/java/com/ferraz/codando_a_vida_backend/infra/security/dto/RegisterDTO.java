package com.ferraz.codando_a_vida_backend.infra.security.dto;

import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO(
        @NotBlank
        String name,
        @NotBlank @Email
        String email,
        @NotBlank
        String password,
        @NotBlank
        String confirmPassword
) implements NewAuditableDTO {
}
