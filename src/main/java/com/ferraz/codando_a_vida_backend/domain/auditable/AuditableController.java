package com.ferraz.codando_a_vida_backend.domain.auditable;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Collection;
import java.util.List;

/*
T: Classe
X: ClasseDTO
Y: NewClasseDTO
Z: UpdateClasseDTO
R: Repository
 */
public abstract class AuditableController<T extends AuditableEntity, D, N extends NewAuditableDTO, U extends UpdateAuditableDTO,
        R extends AuditableRepository<T>> {

    private final Class<T> instanceClass;
    private final AuditableService<T, R> service;
    private final String path;
    private final Class<D> dtoClass;

    public AuditableController(Class<T> instanceClass, AuditableService<T, R> service, String path, Class<D> dtoClass) {
        this.instanceClass = instanceClass;
        this.service = service;
        this.path = path;
        this.dtoClass = dtoClass;
    }

    private D dtoNewInstance(T obj) {
        try {
            return dtoClass.getDeclaredConstructor(instanceClass).newInstance(obj);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<D> create(@RequestBody @Valid N newAuditableDTO, UriComponentsBuilder uriComponentsBuilder) throws NewAuditableException {
        T obj = service.create(newAuditableDTO);
        URI uri = uriComponentsBuilder.path("/{path}/{id}").buildAndExpand(path, obj.getId()).toUri();
        return ResponseEntity.created(uri).body(dtoNewInstance(obj));
    }

    @GetMapping
    public ResponseEntity<Collection<D>> list() {
        List<D> list = service.findAll().stream().map(this::dtoNewInstance).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> findById(@PathVariable Integer id) {
        T obj = service.findById(id);
        return ResponseEntity.ok(dtoNewInstance(obj));
    }

    @PutMapping("/{id}")
    public ResponseEntity<D> update(@PathVariable Integer id, @RequestBody @Valid U updateAuditableDTO) {
        T obj = service.update(id, updateAuditableDTO);
        return ResponseEntity.ok(dtoNewInstance(obj));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
    
}
