package com.ferraz.codando_a_vida_backend.domain.post.dto;

import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.category.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewPostDTO(
        @NotBlank
        String path,
        @NotBlank
        String title,
        @NotBlank
        String body,
        @NotNull
        Category category
) implements NewAuditableDTO {
}
