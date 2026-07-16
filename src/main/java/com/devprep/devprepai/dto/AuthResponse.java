package com.devprep.devprepai.dto;

public record AuthResponse(
        String token,
        String tokenType,
        String email,
        String role
) {
}
