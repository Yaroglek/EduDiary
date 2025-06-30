package com.github.yaroglek.edudiary.extern.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentDto extends UserDto<StudentDto> {

    @NotNull
    private Long schoolClassId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> parentIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> markIds;
}

