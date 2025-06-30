package com.github.yaroglek.edudiary.extern.assembler;

public interface EntityAssembler<D, E> {
    E toEntity(D dto);
}
