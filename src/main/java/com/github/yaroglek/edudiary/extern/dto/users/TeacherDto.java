package com.github.yaroglek.edudiary.extern.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherDto extends UserDto<TeacherDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> subjectIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> classSubjectIds;
}

