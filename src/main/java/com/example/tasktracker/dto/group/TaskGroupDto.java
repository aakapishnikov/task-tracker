package com.example.tasktracker.dto.group;

import lombok.AllArgsConstructor;
import lombok.Getter;
import io.swagger.v3.oas.annotations.media.Schema;

// DTO группы задач
@Getter
@AllArgsConstructor
public class TaskGroupDto {

    @Schema(description = "Идентификатор группы задач", example = "1")
    private Long id;

    @Schema(description = "Название группы задач", example = "Учёба по Java")
    private String name;
}
