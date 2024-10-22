package com.ferraz.codando_a_vida_backend.domain.post;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableEntity;
import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.auditable.UpdateAuditableDTO;
import com.ferraz.codando_a_vida_backend.domain.category.Category;
import com.ferraz.codando_a_vida_backend.domain.post.dto.NewPostDTO;
import com.ferraz.codando_a_vida_backend.domain.post.dto.UpdatePostDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_POSTS")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Post extends AuditableEntity {

    @Column(name = "PATH")
    private String path;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "body")
    private String body;

    @ManyToOne
    @JoinColumn(name = "CATEGORY")
    private Category category;


    @Override
    public <T extends NewAuditableDTO> void create(T dto) {
        NewPostDTO newPostDTO = (NewPostDTO) dto;

        this.path = newPostDTO.path();
        this.title = newPostDTO.title();
        this.body = newPostDTO.body();
        this.category = newPostDTO.category();
    }

    @Override
    public <T extends UpdateAuditableDTO> void update(T dto) {
        UpdatePostDTO updatePostDTO = (UpdatePostDTO) dto;

        this.path = updatePostDTO.path();
        this.title = updatePostDTO.title();
        this.body = updatePostDTO.body();
        this.category = updatePostDTO.category();
    }
}
