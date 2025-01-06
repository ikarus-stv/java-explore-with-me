package ru.practicum.ewm.personal.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.*;
import ru.practicum.ewm.personal.service.PersonalUserEventService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events")
public class PersonalUsersEventController {

    private final PersonalUserEventService personalUserEventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@PathVariable Long userId,
                             @RequestBody @Valid NewEventDto request) {
        log.info("POST /users/{}/events - {}", userId, request);
        return personalUserEventService.save(userId, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> getUserEvents(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("GET /users/{}/events from = {}, size = {}", userId, from, size);
        return personalUserEventService.getAll(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getUserEvent(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("GET /users/{}/events/{}", userId, eventId);
        return personalUserEventService.get(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ParticipationRequestDto> getUserEventRequests(@PathVariable Long userId,
                                                                    @PathVariable Long eventId) {
        log.info("GET /users/{}/events/{}/requests", userId, eventId);
        return personalUserEventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @RequestBody @Valid UpdateEventUserRequest request) {
        log.info("PATCH /users/{}/events/{}", userId, eventId);
        return personalUserEventService.update(userId, eventId, request);
    }

    @PatchMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public EventRequestStatusUpdateResult updateRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("PATCH /users/{}/events/{} new status = {}", userId, eventId, request.getStatus());
        return personalUserEventService.updateRequestsStatus(userId, eventId, request);
    }
}
