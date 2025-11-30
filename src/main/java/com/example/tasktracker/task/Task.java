package com.example.tasktracker.task;

import com.example.tasktracker.group.TaskGroup;
import com.example.tasktracker.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// Сущность задачи
@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Название задачи
    @Column(nullable = false)
    private String title;

    // Описание задачи
    @Column(length = 2000)
    private String description;

    // Статус задачи
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PLANNED;

    // Владелец задачи
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Группа задач
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private TaskGroup group;

    // Дата создания
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Дата обновления
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Хук перед сохранением новой сущности
    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    // Хук перед обновлением
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
