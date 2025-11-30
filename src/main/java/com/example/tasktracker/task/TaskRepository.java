package com.example.tasktracker.task;

import com.example.tasktracker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

// Репозиторий задач
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Все задачи пользователя
    List<Task> findByOwner(User owner);

    // Статистика по статусам задач (JPQL)
    @Query("select t.status as status, count(t) as count from Task t group by t.status")
    List<TaskStatusStats> getStatusStatistics();
}
