package com.ferraz.codando_a_vida_backend.domain.post;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableService;
import com.ferraz.codando_a_vida_backend.domain.post.validator.create.NewPostValidator;
import com.ferraz.codando_a_vida_backend.domain.post.validator.update.UpdatePostValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService extends AuditableService<Post, PostRepository> {

    public PostService(PostRepository repository,
                          List<NewPostValidator> newAuditableValidators,
                          List<UpdatePostValidator> updateAuditableValidators) {
        super(Post.class, repository, newAuditableValidators, updateAuditableValidators);
    }

}
