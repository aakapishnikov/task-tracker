package com.example.tasktracker.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

// Конфигурация OpenAPI/Swagger.
// Здесь задаём базовую информацию об API и описываем схему безопасности для JWT.
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Task Tracker API",
                version = "1.0",
                description = "Учебный backend-сервис для трекинга задач с разделением ролей USER и ADMIN."
        )
)
@SecurityScheme(
        name = "BearerAuth",              // Имя схемы безопасности
        type = SecuritySchemeType.HTTP,   // Тип схемы — HTTP
        scheme = "bearer",                // Используем Bearer-токены
        bearerFormat = "JWT"              // Формат токена
)
public class OpenApiConfig {
    // Дополнительная логика не нужна — всё настраивается через аннотации.
}