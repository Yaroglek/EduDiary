package com.github.yaroglek.edudiary.extern.assembler;

import com.github.yaroglek.edudiary.domain.ClassSubject;
import com.github.yaroglek.edudiary.domain.SchoolClass;
import com.github.yaroglek.edudiary.domain.users.Student;
import com.github.yaroglek.edudiary.extern.controller.SchoolClassController;
import com.github.yaroglek.edudiary.extern.dto.SchoolClassDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class SchoolClassAssembler extends RepresentationModelAssemblerSupport<SchoolClass, SchoolClassDto>
        implements EntityAssembler<SchoolClassDto, SchoolClass> {

    public SchoolClassAssembler() {
        super(SchoolClassController.class, SchoolClassDto.class);
    }

    @Override
    public SchoolClassDto toModel(SchoolClass entity) {
        SchoolClassDto dto = instantiateModel(entity);

        dto.setId(entity.getId());
        dto.setGrade(entity.getGrade());
        dto.setLiteral(entity.getLiteral());
        dto.setStudentIds(entity.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toSet()));
        dto.setClassSubjectIds(entity.getClassSubjects().stream()
                .map(ClassSubject::getId)
                .collect(Collectors.toSet()));

        return dto;
    }

    @Override
    public SchoolClass toEntity(SchoolClassDto dto) {
        return SchoolClass.builder()
                .grade(dto.getGrade())
                .literal(dto.getLiteral())
                .students(new HashSet<>())
                .classSubjects(new HashSet<>())
                .build();
    }
}
