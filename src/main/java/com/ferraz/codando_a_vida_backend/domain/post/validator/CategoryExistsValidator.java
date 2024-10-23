package com.ferraz.codando_a_vida_backend.domain.post.validator;

import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.auditable.UpdateAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.category.Category;
import com.ferraz.codando_a_vida_backend.domain.category.CategoryRepository;
import com.ferraz.codando_a_vida_backend.domain.post.dto.NewPostDTO;
import com.ferraz.codando_a_vida_backend.domain.post.dto.UpdatePostDTO;
import com.ferraz.codando_a_vida_backend.domain.post.validator.create.NewPostValidator;
import com.ferraz.codando_a_vida_backend.domain.post.validator.update.UpdatePostValidator;
import com.ferraz.codando_a_vida_backend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class CategoryExistsValidator implements NewPostValidator, UpdatePostValidator {

    private final CategoryRepository categoryRepository;

    public CategoryExistsValidator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public <T extends NewAuditableDTO> void validate(T dto) {
        NewPostDTO newPostDTO = (NewPostDTO) dto;
        validate(newPostDTO.category());
    }

    @Override
    public <T extends UpdateAuditableDTO> void validate(T dto, Integer id) {
        UpdatePostDTO updatePostDTO = (UpdatePostDTO) dto;
        validate(updatePostDTO.category());
    }

    private void validate(Category category) {
        if (category.getId() == null) {
            throw new ValidationException("category.id", "O id da categoria não pode ser nulo.");
        }

        if (categoryRepository.findById(category.getId()).isEmpty()) {
            throw new ValidationException("category.id", "Nenhuma categoria encontrada para o id " + category.getId());
        }
    }

}
