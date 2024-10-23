package com.ferraz.codando_a_vida_backend.util;

import com.ferraz.codando_a_vida_backend.domain.category.Category;
import com.ferraz.codando_a_vida_backend.domain.category.CategoryRepository;
import com.ferraz.codando_a_vida_backend.domain.category.dto.NewCategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.user.User;

import java.util.Random;

public class CategoryUtil {

    public static Category createValidCategory(User user, CategoryRepository categoryRepository) {
        NewCategoryDTO newCategoryDTO = new NewCategoryDTO("Categoria " + new Random().nextInt(10000));
        Category category = new Category();
        category.create(newCategoryDTO, user);
        return categoryRepository.save(category);
    }

}
