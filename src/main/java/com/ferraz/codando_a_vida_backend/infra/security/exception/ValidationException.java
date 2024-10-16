package com.ferraz.codando_a_vida_backend.infra.security.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ValidationException extends RuntimeException {

    private final List<ValidationExceptionDataDTO> validationExceptionList;

    public ValidationException(String field, String message) {
        super(message);
        this.validationExceptionList = List.of(new ValidationExceptionDataDTO(field, message));
    }

}
