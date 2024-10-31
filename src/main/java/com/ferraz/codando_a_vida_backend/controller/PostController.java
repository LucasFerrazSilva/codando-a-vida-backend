package com.ferraz.codando_a_vida_backend.controller;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableController;
import com.ferraz.codando_a_vida_backend.domain.post.Post;
import com.ferraz.codando_a_vida_backend.domain.post.PostRepository;
import com.ferraz.codando_a_vida_backend.domain.post.PostService;
import com.ferraz.codando_a_vida_backend.domain.post.dto.NewPostDTO;
import com.ferraz.codando_a_vida_backend.domain.post.dto.PostDTO;
import com.ferraz.codando_a_vida_backend.domain.post.dto.UpdatePostDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController extends AuditableController<Post, PostDTO, NewPostDTO, UpdatePostDTO, PostRepository> {

    private final PostService service;
    
    public PostController(PostService service) {
        super(Post.class, service, "/post", PostDTO.class);
        this.service = service;
    }

    @GetMapping("/find-by-category/{categoryName}")
    public ResponseEntity<List<PostDTO>> findByCategory(@PathVariable String categoryName) {
        List<Post> posts = service.findByCategoryName(categoryName);
        List<PostDTO> list = posts.stream().map(PostDTO::new).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/find-by-path/{postPath}")
    public ResponseEntity<PostDTO> findByPostPath(@PathVariable String postPath) {
        Post post = service.findByPostPath(postPath);
        PostDTO dto = new PostDTO(post);
        return ResponseEntity.ok(dto);
    }

}
