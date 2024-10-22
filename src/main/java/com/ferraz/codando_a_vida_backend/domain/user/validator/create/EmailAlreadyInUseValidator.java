package com.ferraz.codando_a_vida_backend.domain.user.validator.create;

import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.user.UserRepository;
import com.ferraz.codando_a_vida_backend.domain.user.exception.EmailAlreadyInUseException;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import org.springframework.stereotype.Component;

@Component
public class EmailAlreadyInUseValidator implements NewUserValidator {

    private final UserRepository repository;

    public EmailAlreadyInUseValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public <T extends NewAuditableDTO> void validate(T dto) {
        RegisterDTO registerDTO = (RegisterDTO) dto;

        if (repository.findByEmail(registerDTO.email()) != null)
            throw new EmailAlreadyInUseException();
    }

}
