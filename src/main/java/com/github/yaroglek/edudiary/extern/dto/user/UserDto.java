package com.github.yaroglek.edudiary.extern.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class UserDto<T extends UserDto<T>> extends RepresentationModel<T> {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank
    private String password;

    @NotBlank
    @Size(max = 100)
    private String fullName;

    @Email
    @Size(max = 100)
    private String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;
}
