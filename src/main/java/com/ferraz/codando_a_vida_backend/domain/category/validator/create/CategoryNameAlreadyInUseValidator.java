package com.ferraz.codando_a_vida_backend.domain.category.validator.create;

import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.category.CategoryRepository;
import com.ferraz.codando_a_vida_backend.domain.category.dto.NewCategoryDTO;
import com.ferraz.codando_a_vida_backend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class CategoryNameAlreadyInUseValidator implements NewCategoryValidator {

    private final CategoryRepository repository;

    public CategoryNameAlreadyInUseValidator(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends NewAuditableDTO> void validate(T dto) {
        NewCategoryDTO newCategoryDTO = (NewCategoryDTO) dto;
        if (!repository.findByNameAndStatus(newCategoryDTO.name(), EntityStatus.ACTIVE).isEmpty()) {
            throw new ValidationException("name", "Esse nome de categoria já está em uso.");
        }
    }
}
