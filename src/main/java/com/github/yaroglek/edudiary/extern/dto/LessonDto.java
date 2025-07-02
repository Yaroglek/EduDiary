package com.github.yaroglek.edudiary.extern.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class LessonDto extends RepresentationModel<LessonDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Range(min = 1, max = 10)
    private Integer lessonNumber;

    @Size(max = 500)
    private String homeworkDescription;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long scheduleDayId;

    @NotNull
    private Long classSubjectId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> markIds;
}

