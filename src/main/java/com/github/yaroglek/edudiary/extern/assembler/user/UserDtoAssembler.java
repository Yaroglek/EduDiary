package com.github.yaroglek.edudiary.extern.assembler.user;

import com.github.yaroglek.edudiary.extern.assembler.EntityAssembler;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public interface UserDtoAssembler<E, D extends RepresentationModel<?>> extends EntityAssembler<D, E>, RepresentationModelAssembler<E, D> {
}
