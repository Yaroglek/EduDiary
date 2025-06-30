package com.github.yaroglek.edudiary.extern.assembler.user;

import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.Subject;
import com.github.yaroglek.edudiary.domain.users.Teacher;
import com.github.yaroglek.edudiary.extern.controller.user.TeacherController;
import com.github.yaroglek.edudiary.extern.dto.user.TeacherDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TeacherAssembler extends UserAssembler<Teacher, TeacherDto> {

    public TeacherAssembler() {
        super(TeacherController.class, TeacherDto.class);
    }

    @Override
    public TeacherDto toModel(Teacher entity) {
        TeacherDto dto = super.toModel(entity);

        dto.setSubjectIds(entity.getSubjects().stream()
                .map(Subject::getId).collect(Collectors.toSet()));

        dto.setClassSubjectIds(entity.getClassSubjects().stream()
                .map(ClassSubject::getId).collect(Collectors.toSet()));
        return dto;
    }

    public Teacher toEntity(TeacherDto dto) {
        Teacher teacher = new Teacher();
        super.toEntity(dto, teacher);
        return teacher;
    }
}

