package com.example.tasktracker.controller;

import com.example.tasktracker.dto.group.CreateTaskGroupRequest;
import com.example.tasktracker.dto.group.TaskGroupDto;
import com.example.tasktracker.dto.group.UpdateTaskGroupRequest;
import com.example.tasktracker.dto.task.CreateTaskRequest;
import com.example.tasktracker.dto.task.TaskDto;
import com.example.tasktracker.dto.task.UpdateTaskRequest;
import com.example.tasktracker.service.TaskGroupService;
import com.example.tasktracker.service.TaskService;
import com.example.tasktracker.user.User;
import com.example.tasktracker.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Импорты Swagger
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

// Контроллер задач и групп текущего пользователя
@Tag(
        name = "Tasks",
        description = "Операции с задачами и группами задач текущего пользователя (роль USER или ADMIN)."
)
@SecurityRequirement(name = "BearerAuth") // Требуется JWT-токен для всех эндпоинтов этого контроллера
@RestController
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;
    private final TaskGroupService taskGroupService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService,
                          TaskGroupService taskGroupService,
                          UserRepository userRepository) {
        this.taskService = taskService;
        this.taskGroupService = taskGroupService;
        this.userRepository = userRepository;
    }

    // Получение списка задач текущего пользователя
    @Operation(
            summary = "Список задач текущего пользователя",
            description = "Возвращает все задачи, принадлежащие пользователю, к которому привязан токен.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список задач успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDto>> getMyTasks(Authentication authentication) {
        User user = getCurrentUser(authentication);
        return ResponseEntity.ok(taskService.getUserTasks(user));
    }

    // Создание новой задачи
    @Operation(
            summary = "Создать задачу",
            description = "Создаёт новую задачу для текущего пользователя. "
                    + "Можно указать статус и группу (groupId), если группа принадлежит этому пользователю.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @PostMapping("/tasks")
    public ResponseEntity<TaskDto> createTask(Authentication authentication,
                                              @Valid @RequestBody CreateTaskRequest request) {
        User user = getCurrentUser(authentication);
        return ResponseEntity.ok(taskService.createTask(user, request));
    }

    // Обновление задачи
    @Operation(
            summary = "Обновить задачу",
            description = "Обновляет выбранные поля задачи (название, описание, статус, группу). "
                    + "Пользователь может изменять только свои задачи.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные или задача не найдена"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @PutMapping("/tasks/{id}")
    public ResponseEntity<TaskDto> updateTask(Authentication authentication,
                                              @PathVariable Long id,
                                              @RequestBody UpdateTaskRequest request) {
        User user = getCurrentUser(authentication);
        return ResponseEntity.ok(taskService.updateTask(user, id, request));
    }

    // Удаление задачи
    @Operation(
            summary = "Удалить задачу",
            description = "Удаляет задачу текущего пользователя по её идентификатору.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
                    @ApiResponse(responseCode = "400", description = "Задача не найдена"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(Authentication authentication,
                                           @PathVariable Long id) {
        User user = getCurrentUser(authentication);
        taskService.deleteTask(user, id);
        return ResponseEntity.noContent().build();
    }

    // Список групп текущего пользователя
    @Operation(
            summary = "Список групп задач",
            description = "Возвращает все группы задач, созданные текущим пользователем.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список групп успешно получен"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @GetMapping("/groups")
    public ResponseEntity<List<TaskGroupDto>> getMyGroups(Authentication authentication) {
        User user = getCurrentUser(authentication);
        return ResponseEntity.ok(taskGroupService.getUserGroups(user));
    }

    // Создание группы задач
    @Operation(
            summary = "Создать группу задач",
            description = "Создаёт новую группу задач для текущего пользователя.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Группа успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Ошибка в данных запроса"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @PostMapping("/groups")
    public ResponseEntity<TaskGroupDto> createGroup(Authentication authentication,
                                                    @Valid @RequestBody CreateTaskGroupRequest request) {
        User user = getCurrentUser(authentication);
        return ResponseEntity.ok(taskGroupService.createGroup(user, request));
    }

    // Обновление группы задач
    @Operation(
            summary = "Обновить группу задач",
            description = "Переименовывает группу задач текущего пользователя.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Группа успешно обновлена"),
                    @ApiResponse(responseCode = "400", description = "Группа не найдена или данные некорректны"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @PutMapping("/groups/{id}")
    public ResponseEntity<TaskGroupDto> updateGroup(Authentication authentication,
                                                    @PathVariable Long id,
                                                    @Valid @RequestBody UpdateTaskGroupRequest request) {
        User user = getCurrentUser(authentication);
        return ResponseEntity.ok(taskGroupService.updateGroup(user, id, request));
    }

    // Удаление группы задач
    @Operation(
            summary = "Удалить группу задач",
            description = "Удаляет группу задач текущего пользователя. "
                    + "Если к ней привязаны задачи, поведение зависит от настроек JPA.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Группа успешно удалена"),
                    @ApiResponse(responseCode = "400", description = "Группа не найдена"),
                    @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
            }
    )
    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(Authentication authentication,
                                            @PathVariable Long id) {
        User user = getCurrentUser(authentication);
        taskGroupService.deleteGroup(user, id);
        return ResponseEntity.noContent().build();
    }

    // Получение текущего пользователя из Authentication
    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Current user not found"));
    }
}
