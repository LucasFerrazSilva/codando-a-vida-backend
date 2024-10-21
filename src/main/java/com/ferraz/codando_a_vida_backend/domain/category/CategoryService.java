package com.ferraz.codando_a_vida_backend.domain.category;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableService;
import com.ferraz.codando_a_vida_backend.domain.category.validator.create.NewCategoryValidator;
import com.ferraz.codando_a_vida_backend.domain.category.validator.update.UpdateCategoryValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends AuditableService<Category, CategoryRepository> {

    public CategoryService(CategoryRepository repository, List<NewCategoryValidator> newCategoryValidators,
                           List<UpdateCategoryValidator> updateCategoryValidators) {

        super(Category.class, repository, newCategoryValidators, updateCategoryValidators);
    }

}
