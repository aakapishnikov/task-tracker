package com.example.tasktracker.dto.group;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

// Запрос на создание группы задач
@Getter
@Setter
public class CreateTaskGroupRequest {

    @NotBlank
    @Schema(description = "Название новой группы задач", example = "Учёба по Java")
    private String name;
}
