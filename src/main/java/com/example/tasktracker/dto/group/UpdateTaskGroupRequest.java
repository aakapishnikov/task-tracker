package com.example.tasktracker.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

// Запрос на обновление группы задач
@Getter
@Setter
public class UpdateTaskGroupRequest {

    @NotBlank
    @Schema(description = "Новое название группы задач", example = "Spring Boot")
    private String name;
}
