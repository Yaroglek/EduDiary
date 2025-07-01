package com.github.yaroglek.edudiary.extern.assembler;

import com.github.yaroglek.edudiary.app.service.ClassSubjectService;
import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.extern.controller.LessonController;
import com.github.yaroglek.edudiary.extern.dto.LessonDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LessonAssembler extends RepresentationModelAssemblerSupport<Lesson, LessonDto>
        implements EntityAssembler<LessonDto, Lesson> {

    private final ClassSubjectService classSubjectService;

    public LessonAssembler(ClassSubjectService classSubjectService) {
        super(LessonController.class, LessonDto.class);
        this.classSubjectService = classSubjectService;
    }

    @Override
    public LessonDto toModel(Lesson entity) {
        LessonDto dto = instantiateModel(entity);

        dto.setId(entity.getId());
        dto.setLessonNumber(entity.getLessonNumber());
        dto.setHomeworkDescription(entity.getHomeworkDescription());
        dto.setScheduleDayId(entity.getScheduleDay().getId());
        dto.setClassSubjectId(entity.getClassSubject().getId());
        dto.setMarks(entity.getMarks().stream()
                .map(Mark::getId)
                .collect(Collectors.toSet()));

        return dto;
    }

    @Override
    public Lesson toEntity(LessonDto dto) {
        return Lesson.builder()
                .id(dto.getId())
                .lessonNumber(dto.getLessonNumber())
                .homeworkDescription(dto.getHomeworkDescription())
                .classSubject(dto.getClassSubjectId() == null ? null : classSubjectService.getById(dto.getClassSubjectId()))
                .build();
    }
}
