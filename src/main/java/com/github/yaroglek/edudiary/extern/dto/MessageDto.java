package com.github.yaroglek.edudiary.extern.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDto {
    @NotBlank(message = "Message cannot be blank")
    String message;
}