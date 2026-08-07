package com.devprep.dto;

import com.devprep.entity.Category;

public record CategoryResponse(
        Long id,
        String name
) {
}
