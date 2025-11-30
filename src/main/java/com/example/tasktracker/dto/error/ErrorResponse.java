package com.example.tasktracker.dto.error;

import java.time.LocalDateTime;

// Простой DTO для ответа с ошибкой в JSON-формате
public class ErrorResponse {

    // Время, когда произошла ошибка
    private LocalDateTime timestamp;

    // HTTP-статус
    private int status;

    // Краткое сообщение об ошибке
    private String message;

    // Дополнительные детали (опционально)
    private String details;

    public ErrorResponse() {
    }

    public ErrorResponse(LocalDateTime timestamp, int status, String message, String details) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
