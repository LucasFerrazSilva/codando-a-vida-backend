package com.ferraz.codando_a_vida_backend.domain.category;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableRepository;
import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface CategoryRepository extends AuditableRepository<Category> {

    List<Category> findByNameAndStatus(@NotBlank String name, EntityStatus status);

}
