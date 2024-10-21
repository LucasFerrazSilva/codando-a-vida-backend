package com.ferraz.codando_a_vida_backend.domain.user.exception;

import com.ferraz.codando_a_vida_backend.infra.exception.ValidationException;

public class EmailAlreadyInUseException extends ValidationException {
    public EmailAlreadyInUseException() {
        super("email", "O e-mail informado já está cadastrado!");
    }
}
