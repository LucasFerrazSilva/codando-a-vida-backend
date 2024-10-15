package com.ferraz.codando_a_vida_backend.domain.user.dto;

import com.ferraz.codando_a_vida_backend.domain.user.User;
import com.ferraz.codando_a_vida_backend.domain.user.UserStatus;

public record UserDTO(
        Long id,
        String name,
        String email,
        UserStatus status
) {
    public UserDTO(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getStatus()
        );
    }
}
