package com.ferraz.codando_a_vida_backend.controller;

import com.ferraz.codando_a_vida_backend.domain.auditable.NewAuditableException;
import com.ferraz.codando_a_vida_backend.domain.category.Category;
import com.ferraz.codando_a_vida_backend.domain.category.CategoryService;
import com.ferraz.codando_a_vida_backend.domain.category.dto.CategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.category.dto.NewCategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.category.dto.UpdateCategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> create(@RequestBody @Valid NewCategoryDTO newCategoryDTO,
                                         UriComponentsBuilder uriComponentsBuilder) throws NewAuditableException {

        Category category = service.create(newCategoryDTO);
        URI uri = uriComponentsBuilder.path("/category/{id}").buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(uri).body(new CategoryDTO(category));
    }

    @GetMapping
    public ResponseEntity<Collection<CategoryDTO>> list() {
        List<CategoryDTO> list = service.findAll().stream().map(CategoryDTO::new).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Integer id) {
        Category category = service.findById(id);
        return ResponseEntity.ok(new CategoryDTO(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Integer id, @RequestBody @Valid UpdateCategoryDTO updateCategoryDTO) {
        Category category = service.update(id, updateCategoryDTO);
        return ResponseEntity.ok(new CategoryDTO(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
