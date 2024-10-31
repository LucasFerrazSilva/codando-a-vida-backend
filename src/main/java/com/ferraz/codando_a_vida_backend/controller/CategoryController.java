package com.ferraz.codando_a_vida_backend.controller;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableController;
import com.ferraz.codando_a_vida_backend.domain.category.Category;
import com.ferraz.codando_a_vida_backend.domain.category.CategoryRepository;
import com.ferraz.codando_a_vida_backend.domain.category.CategoryService;
import com.ferraz.codando_a_vida_backend.domain.category.dto.CategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.category.dto.NewCategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.category.dto.UpdateCategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.post.Post;
import com.ferraz.codando_a_vida_backend.domain.post.dto.PostDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController extends AuditableController<Category, CategoryDTO, NewCategoryDTO, UpdateCategoryDTO, CategoryRepository> {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        super(Category.class, service, "category", CategoryDTO.class);
        this.service = service;
    }

    @GetMapping("/find-by-name/{name}")
    public ResponseEntity<CategoryDTO> findByPostPath(@PathVariable String name) {
        Category category = service.findByName(name);
        CategoryDTO categoryDTO = new CategoryDTO(category);
        return ResponseEntity.ok(categoryDTO);
    }

}
