package com.ferraz.codando_a_vida_backend.domain.user;

import com.ferraz.codando_a_vida_backend.domain.auditable.AuditableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends AuditableRepository<User> {

    User findByEmail(String email);

}
