package com.ferraz.codando_a_vida_backend.domain.post;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableRepository;
import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import com.ferraz.codando_a_vida_backend.domain.category.Category;

import java.util.List;

public interface PostRepository extends AuditableRepository<Post> {

    List<Post> findByCategoryAndStatus(Category category, EntityStatus status);

    boolean existsByPath(String path);
    boolean existsByPathAndIdNot(String path, Integer id);
    boolean existsByTitle(String path);
    boolean existsByTitleAndIdNot(String path, Integer id);

}
