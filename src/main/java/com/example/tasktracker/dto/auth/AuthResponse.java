package com.example.tasktracker.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

// Ответ с JWT токеном
@Getter
@AllArgsConstructor
public class AuthResponse {

    @Schema(description = "JWT-токен, используемый для авторизации", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
