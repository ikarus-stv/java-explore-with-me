package ru.practicum.ewm.personal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.ParticipationRequestDto;
import ru.practicum.ewm.base.enums.EventStates;
import ru.practicum.ewm.base.enums.EventStatuses;
import ru.practicum.ewm.base.exceptions.BadRequestException;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.exceptions.DataNotFoundException;
import ru.practicum.ewm.base.mapper.RequestMapper;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.Request;
import ru.practicum.ewm.base.model.User;
import ru.practicum.ewm.base.repository.EventRepository;
import ru.practicum.ewm.base.repository.RequestRepository;
import ru.practicum.ewm.base.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalUserRequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    public ParticipationRequestDto save(Long userId, Long eventId) {
        if (userId == null || eventId == null) {
            throw new BadRequestException("Invalid params");
        }

        Event requestedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Event not found id=" +  eventId)));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found id=" + userId));

        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException(String.format("Request requestId = %d, userId = %d  already exists",
                    eventId, userId));
        } else if (requestedEvent.getInitiator().equals(user)) {
            throw new ConflictException("You can't create a request to participate in your event");
        } else if (!requestedEvent.getState().equals(EventStates.PUBLISHED)) {
            throw new ConflictException("You cannot participate in an unpublished event");
        } else if (!requestedEvent.getParticipantLimit().equals(0L) &&
                    requestedEvent.getParticipantLimit().equals(requestedEvent.getConfirmedRequests())) {
            throw new ConflictException("The limit of requests for participation in the event has been reached");
        } else if (!requestedEvent.getRequestModeration()) {
            requestedEvent.setConfirmedRequests(requestedEvent.getConfirmedRequests() + 1);
            eventRepository.save(requestedEvent);
        }

        Request request = RequestMapper.mapToEntity(requestedEvent, user);
        request = requestRepository.save(request);

        return RequestMapper.mapToDto(request);
    }

    public Collection<ParticipationRequestDto> get(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found id=" + userId));
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return RequestMapper.mapToListDto(requests);
    }

    public ParticipationRequestDto update(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("Request requestId = %d, userId = %d  not found", requestId, userId)));
        request.setStatus(EventStatuses.CANCELED);
        request = requestRepository.save(request);
        return RequestMapper.mapToDto(request);
    }
}
