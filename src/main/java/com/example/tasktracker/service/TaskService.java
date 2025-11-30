package com.example.tasktracker.service;

import com.example.tasktracker.dto.task.CreateTaskRequest;
import com.example.tasktracker.dto.task.TaskDto;
import com.example.tasktracker.dto.task.UpdateTaskRequest;
import com.example.tasktracker.group.TaskGroup;
import com.example.tasktracker.group.TaskGroupRepository;
import com.example.tasktracker.task.Task;
import com.example.tasktracker.task.TaskRepository;
import com.example.tasktracker.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Сервис задач
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskGroupRepository groupRepository;

    public TaskService(TaskRepository taskRepository,
                       TaskGroupRepository groupRepository) {
        this.taskRepository = taskRepository;
        this.groupRepository = groupRepository;
    }

    // Все задачи пользователя
    public List<TaskDto> getUserTasks(User owner) {
        return taskRepository.findByOwner(owner)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Создание задачи
    public TaskDto createTask(User owner, CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        task.setOwner(owner);

        if (request.getGroupId() != null) {
            TaskGroup group = loadAndCheckGroup(owner, request.getGroupId());
            task.setGroup(group);
        }

        Task saved = taskRepository.save(task);
        return toDto(saved);
    }

    // Обновление задачи
    public TaskDto updateTask(User owner, Long taskId, UpdateTaskRequest request) {
        Task task = findUserTaskOrThrow(owner, taskId);

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getGroupId() != null) {
            TaskGroup group = loadAndCheckGroup(owner, request.getGroupId());
            task.setGroup(group);
        }

        Task saved = taskRepository.save(task);
        return toDto(saved);
    }

    // Удаление задачи
    public void deleteTask(User owner, Long taskId) {
        Task task = findUserTaskOrThrow(owner, taskId);
        taskRepository.delete(task);
    }

    // Поиск задачи пользователя
    private Task findUserTaskOrThrow(User owner, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if (!task.getOwner().getId().equals(owner.getId())) {
            throw new SecurityException("You cannot access this task");
        }
        return task;
    }

    // Загрузка группы с проверкой владельца
    private TaskGroup loadAndCheckGroup(User owner, Long groupId) {
        TaskGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!group.getOwner().getId().equals(owner.getId())) {
            throw new SecurityException("You cannot use this group");
        }
        return group;
    }

    // Преобразование в DTO
    private TaskDto toDto(Task task) {
        Long groupId = task.getGroup() != null ? task.getGroup().getId() : null;
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                groupId
        );
    }
}
