package com.ferraz.codando_a_vida_backend.domain.auditable;

public interface UpdateAuditableValidator {
    <T extends UpdateAuditableDTO> void validate(T dto, Integer id);
}
