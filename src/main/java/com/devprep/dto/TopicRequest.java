package com.devprep.dto;

import jakarta.validation.constraints.NotBlank;

public record TopicRequest(
        @NotBlank(message = "Title cannot be blank")
        String title,
        @NotBlank(message = "Description cannot be blank")
        String description
) {
}
