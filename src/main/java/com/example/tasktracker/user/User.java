package com.example.tasktracker.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Сущность пользователя
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    // Идентификатор пользователя
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Уникальный email
    @Column(nullable = false, unique = true)
    private String email;

    // Захешированный пароль
    @Column(nullable = false)
    private String password;

    // Роль пользователя
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.USER;
}
