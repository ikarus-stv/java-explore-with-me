package ru.practicum.ewm.base.mapper;

import lombok.experimental.UtilityClass;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.base.dto.*;
import ru.practicum.ewm.base.enums.AdminEventAction;
import ru.practicum.ewm.base.enums.EventStates;
import ru.practicum.ewm.base.enums.UserEventAction;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.Location;
import ru.practicum.ewm.base.model.User;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    public static Event mapToEntity(NewEventDto dto, Category category, User initiator) {
        Event event = new Event();
        event.setAnnotation(dto.getAnnotation());
        event.setCategory(category);
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setInitiator(initiator);
        event.setLocation(new Location(dto.getLocation().getLat(), dto.getLocation().getLon()));
        event.setPaid(dto.hasPaid() ? dto.getPaid() : Boolean.FALSE);
        event.setParticipantLimit(dto.hasParticipantLimit() ? dto.getParticipantLimit() : 0L);
        event.setRequestModeration(dto.hasRequestModeration() ? dto.getRequestModeration() : Boolean.TRUE);
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventStates.PENDING);
        event.setTitle(dto.getTitle());
        event.setViews(0L);
        return event;
    }

    public static Event updateAdminFields(Event event, UpdateEventAdminRequest updateEventAdminRequest,
                                          Category category) {
        if (updateEventAdminRequest.hasAnnotation()) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (category != null) {
            event.setCategory(category);
        }
        if (updateEventAdminRequest.hasDescription()) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.hasEventDate()) {
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }
        if (updateEventAdminRequest.hasLocation()) {
            event.setLocation(new Location(updateEventAdminRequest.getLocation().getLat(),
                    updateEventAdminRequest.getLocation().getLon()));
        }
        if (updateEventAdminRequest.hasPaid()) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.hasParticipantLimit()) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.hasRequestModeration()) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.hasTitle()) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.hasStateAction() &&
                updateEventAdminRequest.getStateAction().equals(AdminEventAction.PUBLISH_EVENT)) {
            event.setState(EventStates.PUBLISHED);
        }
        if (updateEventAdminRequest.hasStateAction() &&
                updateEventAdminRequest.getStateAction().equals(AdminEventAction.REJECT_EVENT)) {
            event.setState(EventStates.CANCELED);
        }
        return event;
    }

    public static Event updateUserFields(Event event, UpdateEventUserRequest updateEventUserRequest,
                                         Category category) {
        if (updateEventUserRequest.hasAnnotation()) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.hasCategory()) {
            event.setCategory(category);
        }

        if (updateEventUserRequest.hasDescription()) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.hasEventDate()) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }

        if (updateEventUserRequest.hasLocation()) {
            event.setLocation(new Location(updateEventUserRequest.getLocation().getLat(),
                    updateEventUserRequest.getLocation().getLon()));
        }

        if (updateEventUserRequest.hasPaid()) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.hasParticipantLimit()) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.hasRequestModeration()) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        if (updateEventUserRequest.hasTitle()) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        if (updateEventUserRequest.hasStateAction() && updateEventUserRequest.getStateAction().equals(UserEventAction.SEND_TO_REVIEW)) {
            event.setState(EventStates.PENDING);
        }

        if (updateEventUserRequest.hasStateAction() && updateEventUserRequest.getStateAction().equals(UserEventAction.CANCEL_REVIEW)) {
            event.setState(EventStates.CANCELED);
        }

        return event;
    }

    public static EventFullDto mapToDto(Event entity) {
        EventFullDto dto = new EventFullDto();
        dto.setId(entity.getId());
        dto.setAnnotation(entity.getAnnotation());
        dto.setCategory(Mappers.getMapper(CategoryMapper.class).mapToDto(entity.getCategory()));
        dto.setConfirmedRequests(entity.getConfirmedRequests());
        dto.setCreatedOn(entity.getCreatedOn());
        dto.setDescription(entity.getDescription());
        dto.setEventDate(entity.getEventDate());
        dto.setInitiator(Mappers.getMapper(UserMapper.class).mapToShortDto(entity.getInitiator()));
        dto.setLocation(new LocationDto(entity.getLocation().getLat(), entity.getLocation().getLon()));
        dto.setPaid(entity.getPaid());
        dto.setParticipantLimit(entity.getParticipantLimit());
        dto.setPublishedOn(entity.getPublishedOn());
        dto.setRequestModeration(entity.getRequestModeration());
        dto.setState(entity.getState());
        dto.setTitle(entity.getTitle());
        dto.setViews(entity.getViews());

        return dto;
    }

    public static EventFullDto mapToDtoWithStat(Event entity, Long views) {
        EventFullDto dto = mapToDto(entity);
        dto.setViews(views);

        return dto;
    }

    public static EventShortDto mapToShortDto(Event entity) {
        EventShortDto dto = new EventShortDto();
        dto.setId(entity.getId());
        dto.setAnnotation(entity.getAnnotation());
        dto.setCategory(Mappers.getMapper(CategoryMapper.class).mapToDto(entity.getCategory()));
        dto.setConfirmedRequests(entity.getConfirmedRequests());
        dto.setEventDate(entity.getEventDate());
        dto.setInitiator(Mappers.getMapper(UserMapper.class).mapToShortDto(entity.getInitiator()));
        dto.setPaid(entity.getPaid());
        dto.setTitle(entity.getTitle());
        dto.setViews(entity.getViews());

        return dto;
    }

    public static EventShortDto mapToShortDtoWithStat(Event entity, Long views) {
        EventShortDto dto = mapToShortDto(entity);
        dto.setViews(views);

        return dto;
    }

    public static List<EventFullDto> mapToListDto(List<Event> listEntity) {
        return listEntity.stream().map(EventMapper::mapToDto).collect(Collectors.toList());
    }

    public static Set<EventShortDto> mapToListShortDto(Set<Event> setEntity) {
        return setEntity.stream().map(EventMapper::mapToShortDto).collect(Collectors.toSet());
    }

    public static Set<EventShortDto> mapToListShortDto(Set<Event> setEntity, Map<Long, Long> statsMap) {
        return setEntity.stream().map(elem -> mapToShortDtoWithStat(elem, statsMap.get(elem.getId()))).collect(Collectors.toSet());
    }
}
