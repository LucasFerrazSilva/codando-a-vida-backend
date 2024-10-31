package com.ferraz.codando_a_vida_backend.domain.post;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableRepository;
import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends AuditableRepository<Post> {

    List<Post> findByCategoryNameAndStatus(String categoryName, EntityStatus status);

    boolean existsByPathAndStatus(String path, EntityStatus status);
    boolean existsByPathAndStatusAndIdNot(String path, EntityStatus status, Integer id);
    boolean existsByTitleAndStatus(String path, EntityStatus status);
    boolean existsByTitleAndStatusAndIdNot(String path, EntityStatus status, Integer id);

    Optional<Post> findByPathAndStatus(String postPath, EntityStatus status);
}
