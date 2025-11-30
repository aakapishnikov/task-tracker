package com.example.tasktracker.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Репозиторий пользователей
public interface UserRepository extends JpaRepository<User, Long> {

    // Поиск по email
    Optional<User> findByEmail(String email);

    // Проверка существования по email
    boolean existsByEmail(String email);
}
