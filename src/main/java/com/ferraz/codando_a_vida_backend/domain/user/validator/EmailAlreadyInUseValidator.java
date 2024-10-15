package com.ferraz.codando_a_vida_backend.domain.user.validator;

import com.ferraz.codando_a_vida_backend.domain.user.UserRepository;
import com.ferraz.codando_a_vida_backend.domain.user.exception.EmailAlreadyInUseException;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import org.springframework.stereotype.Component;

@Component
public class EmailAlreadyInUseValidator implements RegisterUserValidator {

    private final UserRepository repository;

    public EmailAlreadyInUseValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void validate(RegisterDTO registerDTO) {
        if (repository.findByEmail(registerDTO.email()) != null)
            throw new EmailAlreadyInUseException();
    }

}
