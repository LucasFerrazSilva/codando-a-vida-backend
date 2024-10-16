package com.ferraz.codando_a_vida_backend.domain.user.validator;

import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;

public interface RegisterUserValidator {

    void validate(RegisterDTO registerDTO);

}
