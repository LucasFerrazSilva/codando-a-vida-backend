package com.ferraz.codando_a_vida_backend.domain.user.exception;

import com.ferraz.codando_a_vida_backend.infra.exception.ValidationException;

public class PasswordsDontMatchException extends ValidationException {
    public PasswordsDontMatchException() {
        super("confirmPassword", "Os campos de senha devem ser iguais.");
    }
}
