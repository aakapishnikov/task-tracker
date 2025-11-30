package com.example.tasktracker.controller;

import com.example.tasktracker.dto.auth.AuthResponse;
import com.example.tasktracker.dto.auth.LoginRequest;
import com.example.tasktracker.dto.auth.RegisterRequest;
import com.example.tasktracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Аннотации Swagger для описания контроллера и методов
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

// Контроллер аутентификации (регистрация и логин пользователей)
@Tag(
        name = "Auth",
        description = "Регистрация новых пользователей и получение JWT-токена для входа в систему."
)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Внедрение сервиса аутентификации
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Регистрация нового пользователя
    @Operation(
            summary = "Регистрация пользователя",
            description = "Создаёт нового пользователя с ролью USER и возвращает JWT-токен. "
                    + "Полученный токен можно использовать для доступа к защищённым эндпоинтам."
    )
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        // Передаём данные регистрации в сервис и возвращаем ответ с токеном
        return ResponseEntity.ok(authService.register(request));
    }

    // Логин существующего пользователя
    @Operation(
            summary = "Логин пользователя",
            description = "Проверяет email и пароль и возвращает JWT-токен. "
                    + "Подходит как для обычных пользователей (USER), так и для администраторов (ADMIN)."
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        // Передаём данные логина в сервис и возвращаем ответ с токеном
        return ResponseEntity.ok(authService.login(request));
    }
}