package com.github.yaroglek.edudiary.extern.assembler.user;

import com.github.yaroglek.edudiary.domain.users.Parent;
import com.github.yaroglek.edudiary.domain.users.Student;
import com.github.yaroglek.edudiary.extern.controller.user.ParentController;
import com.github.yaroglek.edudiary.extern.dto.user.ParentDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ParentAssembler extends UserAssembler<Parent, ParentDto> {

    public ParentAssembler() {
        super(ParentController.class, ParentDto.class);
    }

    @Override
    public ParentDto toModel(Parent entity) {
        ParentDto dto = super.toModel(entity);

        dto.setChildIds(entity.getChildren().stream()
                .map(Student::getId).collect(Collectors.toSet()));

        return dto;
    }

    public Parent toEntity(ParentDto dto) {
        Parent parent = new Parent();
        super.toEntity(dto, parent);
        return parent;
    }
}

