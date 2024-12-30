package ru.practicum.ewm.base.mapper;

import java.time.LocalDateTime;

public class DateTimeMapper {

    public static LocalDateTime toLocalDateTime(String date) {
        return LocalDateTime.parse(date);
    }

    public static String toStringDate(LocalDateTime date) {
        return date.toString();
    }
}
