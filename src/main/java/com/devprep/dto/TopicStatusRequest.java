package com.devprep.dto;

import com.devprep.enums.TopicStatus;

public record TopicStatusRequest(
        TopicStatus status
) {
}
