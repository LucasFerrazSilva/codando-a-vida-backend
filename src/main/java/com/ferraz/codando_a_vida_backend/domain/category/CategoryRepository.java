package com.ferraz.codando_a_vida_backend.domain.category;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableRepository;
import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends AuditableRepository<Category> {

    List<Category> findByNameAndStatus(@NotBlank String name, EntityStatus status);

    Optional<Category> findByName(@NotBlank String name);

}
