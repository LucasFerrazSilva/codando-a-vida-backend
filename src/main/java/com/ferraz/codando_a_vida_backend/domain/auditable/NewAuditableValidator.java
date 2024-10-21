package com.ferraz.codando_a_vida_backend.domain.auditable;

public interface NewAuditableValidator {
    <T extends NewAuditableDTO> void validate(T dto);
}
