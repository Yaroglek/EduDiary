package com.github.yaroglek.edudiary.extern.assembler;

import com.github.yaroglek.edudiary.domain.Lesson;
import com.github.yaroglek.edudiary.domain.ScheduleDay;
import com.github.yaroglek.edudiary.extern.controller.ScheduleDayController;
import com.github.yaroglek.edudiary.extern.dto.ScheduleDayDto;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ScheduleDayAssembler extends RepresentationModelAssemblerSupport<ScheduleDay, ScheduleDayDto>
        implements EntityAssembler<ScheduleDayDto, ScheduleDay> {

    public ScheduleDayAssembler() {
        super(ScheduleDayController.class, ScheduleDayDto.class);
    }

    @Override
    public ScheduleDayDto toModel(ScheduleDay entity) {
        ScheduleDayDto dto = instantiateModel(entity);

        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setSchoolClassId(entity.getSchoolClass().getId());
        dto.setLessonIds(entity.getLessons().stream()
                .map(Lesson::getId)
                .collect(Collectors.toSet()));
        
        return dto;
    }

    @Override
    public ScheduleDay toEntity(ScheduleDayDto dto) {
        return ScheduleDay.builder()
                .date(dto.getDate())
                .build();
    }
}
