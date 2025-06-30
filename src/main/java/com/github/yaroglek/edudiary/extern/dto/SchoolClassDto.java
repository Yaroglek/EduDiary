package com.github.yaroglek.edudiary.extern.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class SchoolClassDto extends RepresentationModel<SchoolClassDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Min(1)
    @Max(11)
    private Integer grade;

    @NotBlank
    @Size(min = 1, max = 2)
    private String literal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> studentIds;
}

