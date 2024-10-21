package com.ferraz.codando_a_vida_backend.domain.auditable;

import com.ferraz.codando_a_vida_backend.domain.user.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static com.ferraz.codando_a_vida_backend.infra.security.AuthenticationService.getLoggedUser;

@Service
public abstract class AuditableService<T extends AuditableEntity, R extends AuditableRepository<T>> {

    private final Class<T> clazz;
    private final R repository;
    private final List<? extends NewAuditableValidator> newAuditableValidators;
    private final List<? extends UpdateAuditableValidator> updateAuditableValidators;

    protected AuditableService(Class<T> clazz, R repository, List<? extends NewAuditableValidator> newAuditableValidators,
                               List<? extends UpdateAuditableValidator> updateAuditableValidators) {
        this.clazz = clazz;
        this.repository = repository;
        this.newAuditableValidators = newAuditableValidators;
        this.updateAuditableValidators = updateAuditableValidators;
    }

    @Transactional
    public <Y extends NewAuditableDTO> T create(Y dto) throws NewAuditableException {
        try {
            newAuditableValidators.forEach(validator -> validator.validate(dto));
            User loggedUser = getLoggedUser();
            T object = clazz.getDeclaredConstructor().newInstance();
            object.create(dto, loggedUser);
            return repository.save(object);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new NewAuditableException(e.getMessage());
        }
    }

    public List<T> findAll() {
        return repository.findByStatus(EntityStatus.ACTIVE);
    }

    public T findById(Integer id) {
        return repository.findById(id).orElseThrow();
    }

    @Transactional
    public <Y extends UpdateAuditableDTO> T update(Integer id, Y dto) {
        updateAuditableValidators.forEach(validator -> validator.validate(dto));
        T object = repository.findById(id).orElseThrow();
        User loggedUser = getLoggedUser();
        object.update(dto, loggedUser);
        return repository.save(object);
    }

    @Transactional
    public void delete(Integer id) {
        T object = repository.findById(id).orElseThrow();
        User loggedUser = getLoggedUser();
        object.inactivate(loggedUser);
        repository.save(object);
    }

}
