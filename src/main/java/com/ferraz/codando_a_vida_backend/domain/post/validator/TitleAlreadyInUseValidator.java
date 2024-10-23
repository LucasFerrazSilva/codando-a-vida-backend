package com.ferraz.codando_a_vida_backend.domain.post.validator;

import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.auditable.UpdateAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.post.PostRepository;
import com.ferraz.codando_a_vida_backend.domain.post.dto.NewPostDTO;
import com.ferraz.codando_a_vida_backend.domain.post.dto.UpdatePostDTO;
import com.ferraz.codando_a_vida_backend.domain.post.validator.create.NewPostValidator;
import com.ferraz.codando_a_vida_backend.domain.post.validator.update.UpdatePostValidator;
import com.ferraz.codando_a_vida_backend.infra.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class TitleAlreadyInUseValidator implements NewPostValidator, UpdatePostValidator {

    private final PostRepository repository;

    public TitleAlreadyInUseValidator(PostRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends NewAuditableDTO> void validate(T dto) {
        NewPostDTO newPostDTO = (NewPostDTO) dto;
        if(repository.existsByTitle(newPostDTO.title()))
            throw new ValidationException("title", "O título especificado já está em uso.");
    }

    @Override
    public <T extends UpdateAuditableDTO> void validate(T dto, Integer id) {
        UpdatePostDTO updatePostDTO = (UpdatePostDTO) dto;
        if(repository.existsByTitleAndIdNot(updatePostDTO.title(), id))
            throw new ValidationException("path", "O título especificado já está em uso.");
    }
}
