package com.ferraz.codando_a_vida_backend.domain.user;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableService;
import com.ferraz.codando_a_vida_backend.domain.user.validator.create.NewUserValidator;
import com.ferraz.codando_a_vida_backend.domain.user.validator.update.UpdateUserValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends AuditableService<User, UserRepository> {

    public UserService(UserRepository repository, List<NewUserValidator> registerUserValidators,
                       List<UpdateUserValidator> updateUserValidators) {
        super(User.class, repository, registerUserValidators, updateUserValidators);
    }

}
