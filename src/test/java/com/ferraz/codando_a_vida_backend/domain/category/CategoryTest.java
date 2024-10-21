package com.ferraz.codando_a_vida_backend.domain.category;

import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import com.ferraz.codando_a_vida_backend.domain.category.dto.NewCategoryDTO;
import com.ferraz.codando_a_vida_backend.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CategoryTest {

    @Test
    @DisplayName("Deve criar a categoria corretamente")
    void testConstructor() {
        // Given
        User user = new User();
        String name = "teste";
        NewCategoryDTO newCategoryDTO = new NewCategoryDTO(name);

        // When
        Category category = new Category();
        category.create(newCategoryDTO, user);

        // Then
        assertThat(category.getName()).isEqualTo(name);
        assertThat(category.getCreateUser()).isEqualTo(user);
        assertThat(category.getStatus()).isEqualTo(EntityStatus.ACTIVE);
        assertThat(category.getCreateDate()).isNotNull();
    }


}