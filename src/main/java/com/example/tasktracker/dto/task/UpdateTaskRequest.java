package com.example.tasktracker.dto.task;

import com.example.tasktracker.task.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

// Запрос на обновление задачи
@Getter
@Setter
public class UpdateTaskRequest {

    @Schema(description = "Новое название задачи", example = "Разобраться с JWT и Spring Security")
    private String title;

    @Schema(description = "Новое описание задачи", example = "Детальнее изучить фильтры и конфигурацию")
    private String description;

    @Schema(description = "Новый статус задачи", example = "IN_PROGRESS")
    private TaskStatus status;

    @Schema(description = "Новая группа задач (опционально)", example = "1")
    private Long groupId;
}
