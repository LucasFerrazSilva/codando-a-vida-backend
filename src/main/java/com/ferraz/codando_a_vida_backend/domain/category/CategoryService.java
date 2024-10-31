package com.ferraz.codando_a_vida_backend.domain.category;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableService;
import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import com.ferraz.codando_a_vida_backend.domain.category.validator.create.NewCategoryValidator;
import com.ferraz.codando_a_vida_backend.domain.category.validator.update.UpdateCategoryValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoryService extends AuditableService<Category, CategoryRepository> {

    public CategoryService(CategoryRepository repository, List<NewCategoryValidator> newCategoryValidators,
                           List<UpdateCategoryValidator> updateCategoryValidators) {

        super(Category.class, repository, newCategoryValidators, updateCategoryValidators);
    }

    public Category findByName(String name) {
        List<Category> categories = repository.findByNameAndStatus(name, EntityStatus.ACTIVE);

        if (categories.isEmpty())
            throw new NoSuchElementException();

        return categories.getFirst();
    }

}
