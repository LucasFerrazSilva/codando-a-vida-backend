package com.ferraz.codando_a_vida_backend.domain.user;

import com.ferraz.codando_a_vida_backend.domain.user.validator.RegisterUserValidator;
import com.ferraz.codando_a_vida_backend.infra.security.dto.RegisterDTO;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;
    private final List<RegisterUserValidator> registerUserValidators;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, List<RegisterUserValidator> registerUserValidators, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.registerUserValidators = registerUserValidators;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterDTO registerDTO) {
        registerUserValidators.forEach(validator -> validator.validate(registerDTO));
        User user = new User(registerDTO, passwordEncoder);
        return repository.save(user);
    }

}
