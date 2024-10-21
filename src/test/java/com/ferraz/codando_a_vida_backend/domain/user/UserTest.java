package com.ferraz.codando_a_vida_backend.domain.user;

import com.ferraz.codando_a_vida_backend.domain.auditable.EntityStatus;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("Deve criar o usuario corretamente")
    void testConstructor() {
        // Given
        RegisterDTO registerDTO = new RegisterDTO("Teste", "teste@mail.com", "1234", "1234");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // When
        User user = new User();
        user.create(registerDTO);

        // Then
        assertThat(user.getName()).isEqualTo(registerDTO.name());
        assertThat(user.getEmail()).isEqualTo(registerDTO.email());
        assertThat( passwordEncoder.matches(registerDTO.password(), user.getPassword())).isTrue();

        assertThat(user.getCreateUser()).isNull();
        assertThat(user.getStatus()).isEqualTo(EntityStatus.ACTIVE);
        assertThat(user.getCreateDate()).isNotNull();
    }

}