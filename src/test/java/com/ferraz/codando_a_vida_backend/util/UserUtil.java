package com.ferraz.codando_a_vida_backend.util;

import com.ferraz.codando_a_vida_backend.domain.user.User;
import com.ferraz.codando_a_vida_backend.domain.user.UserRepository;
import com.ferraz.codando_a_vida_backend.domain.user.UserRole;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;

import java.util.Random;

public class UserUtil {

    public static String defaultPassword = "1234";

    public static User createValidUser(UserRepository userRepository) {
        String name = "User" + new Random().nextInt(10000);
        RegisterDTO registerDTO = new RegisterDTO(name, name + "@mail.com", defaultPassword, defaultPassword);
        User user = new User();
        user.create(registerDTO);
        user.setRole(UserRole.ROLE_ADMIN);
        return userRepository.save(user);
    }

}
