package com.example.tasktracker.dto.task;

import com.example.tasktracker.task.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

// DTO задачи
@Getter
@AllArgsConstructor
public class TaskDto {

    @Schema(description = "Идентификатор задачи", example = "1")
    private Long id;

    @Schema(description = "Название задачи", example = "Разобраться с JWT")
    private String title;

    @Schema(description = "Описание задачи", example = "Изучить фильтры и конфигурацию Spring Security")
    private String description;

    @Schema(description = "Статус задачи", example = "PLANNED")
    private TaskStatus status;

    @Schema(description = "Идентификатор группы задач", example = "1")
    private Long groupId;
}
