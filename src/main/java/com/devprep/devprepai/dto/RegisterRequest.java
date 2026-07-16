package com.devprep.devprepai.dto;

public record RegisterRequest(
        String name,
        String email,
        String password,
        String role
) {
}
