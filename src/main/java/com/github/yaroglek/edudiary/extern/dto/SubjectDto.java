package com.github.yaroglek.edudiary.extern.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class SubjectDto extends RepresentationModel<SubjectDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private Set<Long> teacherIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotNull
    private Set<Long> classSubjectIds;
}
