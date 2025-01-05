package ru.practicum.server.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConvertor {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static LocalDateTime string2LocalDateTime(String dateAsString) {
        return LocalDateTime.parse(dateAsString, FORMATTER);
    }

    public static String localDateTime2String(LocalDateTime date) {
        return date.format(FORMATTER);
    }
}
