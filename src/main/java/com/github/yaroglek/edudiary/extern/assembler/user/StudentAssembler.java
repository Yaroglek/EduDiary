package com.github.yaroglek.edudiary.extern.assembler.user;

import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.domain.users.Parent;
import com.github.yaroglek.edudiary.domain.users.Student;
import com.github.yaroglek.edudiary.extern.controller.user.StudentController;
import com.github.yaroglek.edudiary.extern.dto.user.StudentDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class StudentAssembler extends UserAssembler<Student, StudentDto> {

    public StudentAssembler() {
        super(StudentController.class, StudentDto.class);
    }

    @Override
    public StudentDto toModel(Student entity) {
        StudentDto dto = super.toModel(entity);

        dto.setSchoolClassId(entity.getSchoolClass() == null ? null : entity.getSchoolClass().getId());
        dto.setParentIds(entity.getParents().stream()
                .map(Parent::getId).collect(Collectors.toSet()));
        dto.setMarkIds(entity.getMarks().stream()
                .map(Mark::getId).collect(Collectors.toSet()));

        return dto;
    }

    public Student toEntity(StudentDto dto) {
        Student student = new Student();
        super.toEntity(dto, student);
        return student;
    }
}

