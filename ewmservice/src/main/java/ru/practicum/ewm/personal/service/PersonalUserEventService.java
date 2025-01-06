package ru.practicum.ewm.personal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.ParticipationRequestDto;
import ru.practicum.ewm.base.dto.*;
import ru.practicum.ewm.base.enums.EventStates;
import ru.practicum.ewm.base.enums.EventStatuses;
import ru.practicum.ewm.base.exceptions.BadRequestException;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.exceptions.DataNotFoundException;
import ru.practicum.ewm.base.mapper.EventMapper;
import ru.practicum.ewm.base.mapper.RequestMapper;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.Request;
import ru.practicum.ewm.base.model.User;
import ru.practicum.ewm.base.repository.CategoryRepository;
import ru.practicum.ewm.base.repository.EventRepository;
import ru.practicum.ewm.base.repository.RequestRepository;
import ru.practicum.ewm.base.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonalUserEventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;


    private Event findByIdAndInitiatorId(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new DataNotFoundException(
                        String.format("Event id = %d, userid = %d not found", eventId, userId)));
    }

    private void checkDate(LocalDateTime date) {
        if (date != null && date.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Invalid eventDate " + date);
        }
    }

    public EventFullDto save(Long userId, NewEventDto request) {
        checkDate(request.getEventDate());
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found id =" +  userId));
        return EventMapper.mapToDto(eventRepository.save(
                EventMapper.mapToEntity(request, findCategoryById(request.getCategory()), user)));
    }

    public Collection<EventShortDto> getAll(Long userId, Integer from, Integer size) {
        return EventMapper.mapToListShortDto(eventRepository.findAllByInitiatorId(userId,
                PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"))));
    }

    public EventFullDto get(Long userId, Long eventId) {
        return EventMapper.mapToDto(findByIdAndInitiatorId(eventId, userId));
    }

    public Collection<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        findByIdAndInitiatorId(eventId, userId);
        return RequestMapper.mapToListDto(requestRepository.findAllByEventId(eventId));
    }

    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest request) {
        Event findEvent = findByIdAndInitiatorId(eventId, userId);

        checkDate(request.getEventDate());

        if (findEvent.getState().equals(EventStates.PUBLISHED)) {
            throw new ConflictException("Can't update");
        }

        Category category = null;
        if (request.hasCategory() && !findEvent.getCategory().getId().equals(request.getCategory())) {
            category = findCategoryById(request.getCategory());
        }

        Event updatedEvent = EventMapper.updateUserFields(findEvent, request, category);
        return EventMapper.mapToDto(eventRepository.save(updatedEvent));
    }

    public EventRequestStatusUpdateResult updateRequestsStatus(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest request) {
        List<ParticipationRequestDto> confirmedRequests = List.of();
        List<ParticipationRequestDto> rejectedRequests = List.of();

        List<Long> requestIds = request.getRequestIds();
        List<Request> requests = requestRepository.findAllById(requestIds);
        Event findEvent = findByIdAndInitiatorId(eventId, userId);
        String status = request.getStatus();

        if (status.equals(EventStatuses.REJECTED.toString())) {
            boolean isConfirmedRequestExists = requests.stream()
                    .anyMatch(elem -> elem.getStatus().equals(EventStatuses.CONFIRMED));
            if (isConfirmedRequestExists) {
                throw new ConflictException("Confirmed request exists");
            }
            rejectedRequests = requests.stream()
                    .peek(e -> e.setStatus(EventStatuses.REJECTED))
                    .map(RequestMapper::mapToDto)
                    .collect(Collectors.toList());

        } else if (status.equals(EventStatuses.CONFIRMED.toString())) {
            Long participantLimit = findEvent.getParticipantLimit();
            Long approvedRequests = findEvent.getConfirmedRequests();
            long availableParticipants = participantLimit - approvedRequests;
            long waitingParticipants = requestIds.size();

            if (participantLimit > 0 && participantLimit.equals(approvedRequests)) {
                throw new ConflictException("Participant limit reached, event = " + findEvent.getTitle());
            } else if (participantLimit.equals(0L) || (waitingParticipants <= availableParticipants && !findEvent.getRequestModeration())) {
                confirmedRequests = requests.stream()
                        .peek(e -> {
                            if (!e.getStatus().equals(EventStatuses.CONFIRMED)) {
                                e.setStatus(EventStatuses.CONFIRMED);
                            } else {
                                throw new ConflictException("Request already confirmed id = " + e.getId());
                            }
                        })
                        .map(RequestMapper::mapToDto)
                        .toList();

                findEvent.setConfirmedRequests(approvedRequests + waitingParticipants);
            } else {
                confirmedRequests = requests.stream()
                        .limit(availableParticipants)
                        .peek(e -> {
                            if (!e.getStatus().equals(EventStatuses.CONFIRMED)) {
                                e.setStatus(EventStatuses.CONFIRMED);
                            } else {
                                throw new ConflictException("Request already confirmed id = " + e.getId());
                            }
                        })
                        .map(RequestMapper::mapToDto)
                        .toList();

                rejectedRequests = requests.stream()
                        .skip(availableParticipants)
                        .peek(e -> {
                            if (!e.getStatus().equals(EventStatuses.REJECTED)) {
                                e.setStatus(EventStatuses.REJECTED);
                            } else {
                                throw new ConflictException("Request already rejected id = " + e.getId());
                            }
                        })
                        .map(RequestMapper::mapToDto)
                        .toList();

                findEvent.setConfirmedRequests((long) confirmedRequests.size());
            }
        } else {
            throw new ConflictException("Invalid status " + status);
        }

        eventRepository.flush();
        requestRepository.flush();

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private Category findCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category not found id = " + catId));
    }
}
