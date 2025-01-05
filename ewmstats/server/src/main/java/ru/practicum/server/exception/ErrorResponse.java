package ru.practicum.server.exception;

import lombok.Getter;
import ru.practicum.server.mapper.DateTimeConvertor;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final String status;
    private final String reason;
    private final String message;
    private final String timestamp;

    public ErrorResponse(String status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = DateTimeConvertor.localDateTime2String(LocalDateTime.now());
    }
}
