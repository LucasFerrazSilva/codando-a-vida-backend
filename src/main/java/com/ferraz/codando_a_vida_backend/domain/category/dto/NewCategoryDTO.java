package com.ferraz.codando_a_vida_backend.domain.category.dto;

import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import jakarta.validation.constraints.NotBlank;

public record NewCategoryDTO(
        @NotBlank
        String name
) implements NewAuditableDTO {
}
