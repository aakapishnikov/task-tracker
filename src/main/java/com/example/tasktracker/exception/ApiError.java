package com.example.tasktracker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Ответ ошибки API
@Getter
@AllArgsConstructor
public class ApiError {

    private String message;
}
