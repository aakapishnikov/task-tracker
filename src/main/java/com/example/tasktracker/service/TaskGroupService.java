package com.example.tasktracker.service;

import com.example.tasktracker.dto.group.CreateTaskGroupRequest;
import com.example.tasktracker.dto.group.TaskGroupDto;
import com.example.tasktracker.dto.group.UpdateTaskGroupRequest;
import com.example.tasktracker.group.TaskGroup;
import com.example.tasktracker.group.TaskGroupRepository;
import com.example.tasktracker.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Сервис групп задач
@Service
public class TaskGroupService {

    private final TaskGroupRepository taskGroupRepository;

    public TaskGroupService(TaskGroupRepository taskGroupRepository) {
        this.taskGroupRepository = taskGroupRepository;
    }

    // Список групп пользователя
    public List<TaskGroupDto> getUserGroups(User owner) {
        return taskGroupRepository.findByOwner(owner)
                .stream()
                .map(group -> new TaskGroupDto(group.getId(), group.getName()))
                .collect(Collectors.toList());
    }

    // Создание группы
    public TaskGroupDto createGroup(User owner, CreateTaskGroupRequest request) {
        TaskGroup group = new TaskGroup();
        group.setName(request.getName());
        group.setOwner(owner);

        TaskGroup saved = taskGroupRepository.save(group);
        return new TaskGroupDto(saved.getId(), saved.getName());
    }

    // Обновление группы
    public TaskGroupDto updateGroup(User owner, Long groupId, UpdateTaskGroupRequest request) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!group.getOwner().getId().equals(owner.getId())) {
            throw new SecurityException("You cannot modify this group");
        }

        group.setName(request.getName());
        TaskGroup saved = taskGroupRepository.save(group);
        return new TaskGroupDto(saved.getId(), saved.getName());
    }

    // Удаление группы
    public void deleteGroup(User owner, Long groupId) {
        TaskGroup group = taskGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (!group.getOwner().getId().equals(owner.getId())) {
            throw new SecurityException("You cannot delete this group");
        }

        taskGroupRepository.delete(group);
    }
}
