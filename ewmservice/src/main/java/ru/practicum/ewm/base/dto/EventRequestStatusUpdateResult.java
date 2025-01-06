package ru.practicum.ewm.base.dto;

import lombok.*;
import ru.practicum.ewm.base.dto.ParticipationRequestDto;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
