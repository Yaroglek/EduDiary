package com.github.yaroglek.edudiary.extern.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentDto extends UserDto<StudentDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long schoolClassId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> parentIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> markIds;
}

