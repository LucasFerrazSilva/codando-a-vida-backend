package com.ferraz.codando_a_vida_backend.infra.exception;

import org.springframework.validation.FieldError;

import java.io.Serializable;

public record ValidationExceptionDataDTO(
    String field,
    String message
) implements Serializable {

    public ValidationExceptionDataDTO(FieldError fieldError) {
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }

}
