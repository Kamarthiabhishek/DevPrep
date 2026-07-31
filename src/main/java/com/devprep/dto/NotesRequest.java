package com.devprep.dto;

import jakarta.validation.constraints.NotBlank;

public record NotesRequest(
        @NotBlank(message = "Content cannot be blank")
        String content
) {
}
