package com.ferraz.codando_a_vida_backend.domain.user.validator;

import com.ferraz.codando_a_vida_backend.domain.user.exception.PasswordsDontMatchException;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import org.springframework.stereotype.Component;

@Component
public class PasswordsMatchValidator implements RegisterUserValidator {

    @Override
    public void validate(RegisterDTO registerDTO) {
        if (!registerDTO.password().equals(registerDTO.confirmPassword()))
            throw new PasswordsDontMatchException();
    }

}
