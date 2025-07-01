package com.github.yaroglek.edudiary.extern.assembler.user;

import com.github.yaroglek.edudiary.domain.users.User;
import com.github.yaroglek.edudiary.extern.dto.user.UserDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public abstract class UserAssembler<T extends User, D extends UserDto<D>>
        extends RepresentationModelAssemblerSupport<T, D> {

    protected UserAssembler(Class<?> controllerClass, Class<D> dtoClass) {
        super(controllerClass, dtoClass);
    }

    @Override
    public D toModel(T entity) {
        D dto = instantiateModel(entity);

        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setFullName(entity.getFullName());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole().name());

        return dto;
    }

    public T toEntity(D dto, T entity) {
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setFullName(dto.getFullName());
        entity.setEmail(dto.getEmail());
        return entity;
    }
}

