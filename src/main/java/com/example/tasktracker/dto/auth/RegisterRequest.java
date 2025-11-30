package com.example.tasktracker.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

// Запрос на регистрацию
@Getter
@Setter
public class RegisterRequest {

    @Email
    @NotBlank
    @Schema(description = "Email пользователя", example = "user@example.com")
    private String email;

    @NotBlank
    @Schema(description = "Пароль пользователя", example = "secret")
    private String password;
}
