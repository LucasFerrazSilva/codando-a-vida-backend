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
public class PathAlreadyInUseValidator implements NewPostValidator, UpdatePostValidator {

    private final PostRepository repository;

    public PathAlreadyInUseValidator(PostRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends NewAuditableDTO> void validate(T dto) {
        NewPostDTO newPostDTO = (NewPostDTO) dto;
        if(repository.existsByPath(newPostDTO.path()))
            throw new ValidationException("path", "O path especificado já está em uso.");
    }

    @Override
    public <T extends UpdateAuditableDTO> void validate(T dto, Integer id) {
        UpdatePostDTO updatePostDTO = (UpdatePostDTO) dto;
        if(repository.existsByPathAndIdNot(updatePostDTO.path(), id))
            throw new ValidationException("path", "O path especificado já está em uso.");
    }
}
