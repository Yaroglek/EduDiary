package com.github.yaroglek.edudiary.extern.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScheduleDayDto extends RepresentationModel<ScheduleDayDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long schoolClassId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<LessonDto> lessons;
}

