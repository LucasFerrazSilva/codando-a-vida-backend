package com.ferraz.codando_a_vida_backend.domain.category.dto;

import com.ferraz.codando_a_vida_backend.domain.auditable.UpdateAuditableDTO;
import jakarta.validation.constraints.NotBlank;

public record UpdateCategoryDTO(
        @NotBlank
        String name
) implements UpdateAuditableDTO {
}
