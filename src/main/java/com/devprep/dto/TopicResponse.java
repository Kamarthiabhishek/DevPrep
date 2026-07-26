package com.devprep.dto;

import com.devprep.enums.TopicStatus;

public record TopicResponse(
        Long id,
        String title,
        String description,
        TopicStatus status
) {
}
