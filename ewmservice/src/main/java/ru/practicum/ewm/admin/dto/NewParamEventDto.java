package ru.practicum.ewm.admin.dto;

import lombok.*;
import ru.practicum.ewm.base.enums.EventStates;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewParamEventDto {
    private List<Long> users;
    private List<EventStates> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Integer from;
    private Integer size;
}
