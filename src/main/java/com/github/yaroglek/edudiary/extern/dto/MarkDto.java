package com.github.yaroglek.edudiary.extern.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class MarkDto extends RepresentationModel<MarkDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Range(min = 1, max = 5)
    private Integer markValue;

    @Size(max = 500)
    private String comment;

    @NotNull
    private Long studentId;

    @NotNull
    private Long lessonId;
}

