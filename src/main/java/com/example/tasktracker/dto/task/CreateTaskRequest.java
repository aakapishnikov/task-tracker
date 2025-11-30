package com.example.tasktracker.dto.task;

import com.example.tasktracker.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

// Запрос на создание задачи
@Getter
@Setter
public class CreateTaskRequest {

    @NotBlank
    @Schema(description = "Название задачи", example = "Разобраться с JWT")
    private String title;

    @Schema(description = "Описание задачи", example = "Изучить фильтры и конфигурацию Spring Security")
    private String description;

    @Schema(description = "Статус задачи", example = "PLANNED")
    private TaskStatus status;

    @Schema(description = "Идентификатор группы задач (опционально)", example = "1")
    private Long groupId;
}
