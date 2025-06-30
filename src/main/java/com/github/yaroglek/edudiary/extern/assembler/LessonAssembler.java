package com.github.yaroglek.edudiary.extern.assembler;

import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.extern.controller.LessonController;
import com.github.yaroglek.edudiary.extern.dto.LessonDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class LessonAssembler extends RepresentationModelAssemblerSupport<Lesson, LessonDto>
        implements EntityAssembler<LessonDto, Lesson> {

    public LessonAssembler() {
        super(LessonController.class, LessonDto.class);
    }

    @Override
    public LessonDto toModel(Lesson entity) {
        LessonDto dto = instantiateModel(entity);
        dto.setId(entity.getId());
        dto.setLessonNumber(entity.getLessonNumber());
        dto.setHomeworkDescription(entity.getHomeworkDescription());
        dto.setScheduleDayId(entity.getScheduleDay().getId());
        dto.setClassSubjectId(entity.getClassSubject().getId());
        return dto;
    }

    @Override
    public Lesson toEntity(LessonDto dto) {
        Lesson lesson = new Lesson();
        lesson.setLessonNumber(dto.getLessonNumber());
        lesson.setHomeworkDescription(dto.getHomeworkDescription());
        return lesson;
    }
}
