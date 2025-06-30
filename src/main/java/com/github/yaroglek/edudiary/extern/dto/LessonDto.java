package com.github.yaroglek.edudiary.extern.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class LessonDto extends RepresentationModel<LessonDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Min(0)
    @Max(10)
    private Integer lessonNumber;

    @Size(max = 500)
    private String homeworkDescription;

    @NotNull
    private Long scheduleDayId;

    @NotNull
    private Long classSubjectId;
}

