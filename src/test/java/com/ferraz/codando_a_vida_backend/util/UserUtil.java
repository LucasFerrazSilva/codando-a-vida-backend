package com.ferraz.codando_a_vida_backend.util;

import com.ferraz.codando_a_vida_backend.domain.user.User;
import com.ferraz.codando_a_vida_backend.domain.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

public class UserUtil {

    public static String defaultPassword = "1234";

    public static User createValidUser(UserRepository userRepository) {
        String name = "User" + new Random().nextInt(10000);
        User user = new User();
        user.setName(name);
        user.setEmail(name + "@mail.com");
        user.setPassword(new BCryptPasswordEncoder().encode(defaultPassword));
        return userRepository.save(user);
    }

}
