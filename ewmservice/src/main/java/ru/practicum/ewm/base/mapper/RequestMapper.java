package ru.practicum.ewm.base.mapper;

import ru.practicum.ewm.base.dto.ParticipationRequestDto;
import ru.practicum.ewm.base.enums.EventStatuses;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.Request;
import ru.practicum.ewm.base.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class RequestMapper {
    public static Request mapToEntity(Event event, User requester) {
        Request result = new Request();

        result.setEvent(event);
        result.setRequester(requester);
        result.setCreated(LocalDateTime.now());
        result.setStatus(!event.getRequestModeration() || event.getParticipantLimit().equals(0L) ? EventStatuses.CONFIRMED : EventStatuses.PENDING);

        return result;
    }

    public static ParticipationRequestDto mapToDto(Request request) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();

        participationRequestDto.setId(request.getId());
        participationRequestDto.setEvent(request.getEvent().getId());
        participationRequestDto.setRequester(request.getRequester().getId());
        participationRequestDto.setCreated(request.getCreated());
        participationRequestDto.setStatus(request.getStatus());

        return participationRequestDto;
    }

    public static List<ParticipationRequestDto> mapToListDto(List<Request> listEntity) {
        return listEntity.stream().map(RequestMapper::mapToDto).collect(Collectors.toList());
    }
}
