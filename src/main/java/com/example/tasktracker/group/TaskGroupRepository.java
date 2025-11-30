package com.example.tasktracker.group;

import com.example.tasktracker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Репозиторий групп задач
public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {

    // Группы конкретного пользователя
    List<TaskGroup> findByOwner(User owner);
}
