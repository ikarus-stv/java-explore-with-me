package ru.practicum.ewm.common.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.EventFullDto;
import ru.practicum.ewm.base.dto.EventShortDto;
import ru.practicum.ewm.common.service.CommonEventsService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class CommonEventsController {
    private final CommonEventsService commonEventsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> getAll(@RequestParam(name = "text", required = false) String text,
                                            @RequestParam(name = "categories", required = false) List<Long> categories,
                                            @RequestParam(name = "paid", required = false) Boolean paid,
                                            @RequestParam(name = "rangeStart", required = false) LocalDateTime rangeStart,
                                            @RequestParam(name = "rangeEnd", required = false) LocalDateTime rangeEnd,
                                            @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
                                            @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("GET /events c параметрами: text = {}, categories = {}, paid = {}, rangeStart = {}, " +
                 "rangeEnd = {}, onlyAvailable = {}, sort = {}, from = {}, size = {}", text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        EventRequestParam params = new EventRequestParam(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
        return commonEventsService.getAll(params);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto get(@PathVariable Long id, HttpServletRequest request) {
        log.info("GET /events/{}", id);
        return commonEventsService.get(id, request);
    }
}
