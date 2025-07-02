package com.github.yaroglek.edudiary.extern.assembler;

import com.github.yaroglek.edudiary.app.service.LessonService;
import com.github.yaroglek.edudiary.app.service.user.StudentService;
import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.extern.assembler.user.StudentAssembler;
import com.github.yaroglek.edudiary.extern.controller.rest.MarkController;
import com.github.yaroglek.edudiary.extern.dto.MarkDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class MarkAssembler extends RepresentationModelAssemblerSupport<Mark, MarkDto>
        implements EntityAssembler<MarkDto, Mark> {

    private final LessonService lessonService;
    private final StudentService studentService;

    public MarkAssembler(LessonService lessonService, StudentService studentService) {
        super(MarkController.class, MarkDto.class);
        this.lessonService = lessonService;
        this.studentService = studentService;
    }

    @Override
    public MarkDto toModel(Mark entity) {
        MarkDto dto = instantiateModel(entity);

        dto.setId(entity.getId());
        dto.setMarkValue(entity.getMarkValue());
        dto.setComment(entity.getComment());
        dto.setLessonId(entity.getLesson().getId());
        dto.setStudentId(entity.getStudent().getId());

        return dto;
    }

    @Override
    public Mark toEntity(MarkDto dto) {
        return Mark.builder()
                .markValue(dto.getMarkValue())
                .lesson(lessonService.getById(dto.getLessonId()))
                .student(studentService.getById(dto.getStudentId()))
                .comment(dto.getComment())
                .build();
    }
}
