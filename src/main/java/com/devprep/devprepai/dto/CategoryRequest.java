package com.devprep.devprepai.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        Long id,
        @NotBlank(message = "Name cannot be blank")
        String name
) {
}
