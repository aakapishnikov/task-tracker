package com.example.tasktracker.task;

// Проекция для статистики по статусам
public interface TaskStatusStats {

    TaskStatus getStatus();

    long getCount();
}
