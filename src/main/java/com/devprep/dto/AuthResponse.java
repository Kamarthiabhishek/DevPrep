package com.devprep.dto;

public record AuthResponse(
        String token,
        String tokenType,
        String email,
        String role
) {
}
