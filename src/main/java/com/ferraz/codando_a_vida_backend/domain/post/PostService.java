package com.ferraz.codando_a_vida_backend.domain.post;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableService;
import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import com.ferraz.codando_a_vida_backend.domain.post.validator.create.NewPostValidator;
import com.ferraz.codando_a_vida_backend.domain.post.validator.update.UpdatePostValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService extends AuditableService<Post, PostRepository> {

    private final PostRepository repository;

    public PostService(PostRepository repository,
                          List<NewPostValidator> newAuditableValidators,
                          List<UpdatePostValidator> updateAuditableValidators) {
        super(Post.class, repository, newAuditableValidators, updateAuditableValidators);
        this.repository = repository;
    }

    public List<Post> findByCategoryName(String categoryName) {
        return repository.findByCategoryNameAndStatus(categoryName, EntityStatus.ACTIVE);
    }

    public Post findByPostPath(String postPath) {
        return repository.findByPathAndStatus(postPath, EntityStatus.ACTIVE).orElseThrow();
    }
}
