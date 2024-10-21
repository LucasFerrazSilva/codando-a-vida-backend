package com.ferraz.codando_a_vida_backend.domain.category.dto;

import com.ferraz.codando_a_vida_backend.domain.category.Category;

public record CategoryDTO(
        Integer id,
        String name
) {
    public CategoryDTO(Category category) {
        this(
                category.getId(),
                category.getName()
        );
    }
}
