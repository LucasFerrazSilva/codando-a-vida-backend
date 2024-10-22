package com.ferraz.codando_a_vida_backend.domain.auditable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AuditableRepository<T> extends JpaRepository<T, Integer> {

    List<T> findByStatus(EntityStatus status);

}
