package com.github.yaroglek.edudiary.extern.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParentDto extends UserDto<ParentDto> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Long> childIds;
}

