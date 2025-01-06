package ru.practicum.ewm.personal.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.ParticipationRequestDto;
import ru.practicum.ewm.personal.service.PersonalUserRequestService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/requests")
public class PersonalUsersRequestController {

    private final PersonalUserRequestService personalUserRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto save(@PathVariable Long userId,
                                        @RequestParam(required = false) Long eventId) {
        log.info("POST /users/{}/requests  event={}", userId, eventId);
        return personalUserRequestService.save(userId, eventId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ParticipationRequestDto> get(@PathVariable Long userId) {
        log.info("GET /users/{}/requests", userId);
        return personalUserRequestService.get(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ParticipationRequestDto update(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        log.info("PATCH /users/{}/requests/{}/cancel", userId, requestId);
        return personalUserRequestService.update(userId, requestId);
    }
}
