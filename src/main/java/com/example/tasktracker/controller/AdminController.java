package com.example.tasktracker.controller;

import com.example.tasktracker.dto.group.TaskGroupDto;
import com.example.tasktracker.group.TaskGroupRepository;
import com.example.tasktracker.task.Task;
import com.example.tasktracker.task.TaskRepository;
import com.example.tasktracker.task.TaskStatusStats;
import com.example.tasktracker.user.User;
import com.example.tasktracker.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// Импорты Swagger
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

// Админские эндпоинты (доступны только пользователям с ролью ADMIN)
@Tag(
        name = "Admin",
        description = "Административные операции: просмотр всех пользователей, задач и статистики. "
                + "Требуется JWT-токен пользователя с ролью ADMIN."
)
@SecurityRequirement(name = "BearerAuth")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final TaskGroupRepository groupRepository;

    public AdminController(UserRepository userRepository,
                           TaskRepository taskRepository,
                           TaskGroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.groupRepository = groupRepository;
    }

    // Список всех пользователей системы
    @Operation(
            summary = "Список всех пользователей",
            description = "Возвращает всех зарегистрированных пользователей. Требуется роль ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список пользователей получен"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа (не ADMIN)")
            }
    )
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    // Список всех задач в системе
    @Operation(
            summary = "Список всех задач",
            description = "Возвращает все задачи в системе независимо от владельца. Требуется роль ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список задач получен"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа (не ADMIN)")
            }
    )
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskRepository.findAll());
    }

    // Список всех групп задач
    @Operation(
            summary = "Список всех групп задач",
            description = "Возвращает все группы задач, созданные всеми пользователями. Требуется роль ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список групп получен"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа (не ADMIN)")
            }
    )
    @GetMapping("/groups")
    public ResponseEntity<List<TaskGroupDto>> getAllGroups() {
        List<TaskGroupDto> groups = groupRepository.findAll()
                .stream()
                .map(g -> new TaskGroupDto(g.getId(), g.getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(groups);
    }

    // Задачи конкретного пользователя
    @Operation(
            summary = "Задачи конкретного пользователя",
            description = "Возвращает задачи выбранного пользователя по его идентификатору (userId). Требуется роль ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список задач пользователя получен"),
                    @ApiResponse(responseCode = "400", description = "Пользователь с таким id не найден"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа (не ADMIN)")
            }
    )
    @GetMapping("/users/{userId}/tasks")
    public ResponseEntity<List<Task>> getUserTasks(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(taskRepository.findByOwner(user));
    }

    // Статистика по статусам задач
    @Operation(
            summary = "Статистика задач по статусам",
            description = "Возвращает количество задач в каждом статусе (PLANNED / IN_PROGRESS / DONE) по всей системе. "
                    + "Требуется роль ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статистика успешно получена"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован"),
                    @ApiResponse(responseCode = "403", description = "Нет прав доступа (не ADMIN)")
            }
    )
    @GetMapping("/stats/statuses")
    public ResponseEntity<List<TaskStatusStats>> getStatusStats() {
        return ResponseEntity.ok(taskRepository.getStatusStatistics());
    }
}
