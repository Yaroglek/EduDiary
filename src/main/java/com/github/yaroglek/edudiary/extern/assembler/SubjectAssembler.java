package com.github.yaroglek.edudiary.extern.assembler;


import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.Subject;
import com.github.yaroglek.edudiary.domain.users.User;
import com.github.yaroglek.edudiary.extern.controller.SubjectController;
import com.github.yaroglek.edudiary.extern.dto.SubjectDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class SubjectAssembler extends RepresentationModelAssemblerSupport<Subject, SubjectDto> implements EntityAssembler<SubjectDto, Subject> {

    public SubjectAssembler() {
        super(SubjectController.class, SubjectDto.class);
    }

    @Override
    public SubjectDto toModel(Subject entity) {
        SubjectDto dto = instantiateModel(entity);

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setTeacherIds(entity.getTeachers().stream().map(User::getId).collect(Collectors.toSet()));
        dto.setClassSubjectIds(entity.getClassSubjects().stream().map(ClassSubject::getId).collect(Collectors.toSet()));

        return dto;
    }

    @Override
    public Subject toEntity(SubjectDto dto) {
        return Subject.builder()
                .name(dto.getName())
                .teachers(new HashSet<>())
                .classSubjects(new HashSet<>())
                .build();
    }
}
