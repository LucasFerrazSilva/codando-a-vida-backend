package com.ferraz.codando_a_vida_backend.domain.post.dto;

import com.ferraz.codando_a_vida_backend.domain.category.dto.CategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.post.Post;

public record PostDTO(
        Integer id,
        String path,
        String title,
        String body,
        CategoryDTO categoryDTO
) {
    public PostDTO(Post post) {
        this(
                post.getId(),
                post.getPath(),
                post.getTitle(),
                post.getBody(),
                new CategoryDTO(post.getCategory())
        );
    }
}
