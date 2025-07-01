package com.github.yaroglek.edudiary.extern.assembler;

import com.github.yaroglek.edudiary.domain.Mark;
import com.github.yaroglek.edudiary.extern.controller.MarkController;
import com.github.yaroglek.edudiary.extern.dto.MarkDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class MarkAssembler extends RepresentationModelAssemblerSupport<Mark, MarkDto>
        implements EntityAssembler<MarkDto, Mark> {

    public MarkAssembler() {
        super(MarkController.class, MarkDto.class);
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
                .comment(dto.getComment())
                .build();
    }
}
