package com.ferraz.codando_a_vida_backend.domain.user.validator.create;

import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.user.exception.PasswordsDontMatchException;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import org.springframework.stereotype.Component;

@Component
public class PasswordsMatchValidator implements NewUserValidator {

    @Override
    public <T extends NewAuditableDTO> void validate(T dto) {
        RegisterDTO registerDTO = (RegisterDTO) dto;

        if (!registerDTO.password().equals(registerDTO.confirmPassword()))
            throw new PasswordsDontMatchException();
    }

}
