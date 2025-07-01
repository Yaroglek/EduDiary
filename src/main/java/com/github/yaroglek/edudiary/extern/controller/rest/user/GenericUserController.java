package com.github.yaroglek.edudiary.extern.controller.rest.user;

import com.github.yaroglek.edudiary.app.service.user.GenericUserService;
import com.github.yaroglek.edudiary.domain.users.User;
import com.github.yaroglek.edudiary.extern.dto.MessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
public abstract class GenericUserController<T extends User, D, S extends GenericUserService<T>, A> {

    protected final S service;
    protected final A assembler;

    @PostMapping
    public ResponseEntity<D> create(@Valid @RequestBody D dto) {
        T entity = toEntity(dto);
        T saved = service.create(entity);
        return ResponseEntity.ok(toModel(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<D> get(@PathVariable Long id) {
        T found = service.getById(id);
        return ResponseEntity.ok(toModel(found));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDto> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new MessageDto(
                getEntityName() + " with ID " + id + " deleted"));
    }

    protected abstract T toEntity(D dto);

    protected abstract D toModel(T entity);

    protected abstract String getEntityName();
}

