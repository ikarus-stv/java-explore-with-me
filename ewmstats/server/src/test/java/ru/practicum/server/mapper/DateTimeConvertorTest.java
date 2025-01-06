package ru.practicum.server.mapper;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DateTimeConvertorTest {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime nowDateTime = LocalDateTime.now();
    private final String nowString = LocalDateTime.now().format(FORMATTER);

    @Test
    public void toLocalDateTimeTest() {
        assertThat(DateTimeConvertor.string2LocalDateTime(nowString), equalTo(LocalDateTime.parse(nowString, FORMATTER)));
    }

    @Test
    public void toStringDateTest() {
        assertThat(DateTimeConvertor.localDateTime2String(nowDateTime), equalTo(nowString));
    }
}
