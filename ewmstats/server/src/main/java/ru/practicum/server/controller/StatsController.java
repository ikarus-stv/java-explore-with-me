package ru.practicum.server.controller;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.exception.BadRequestException;
import ru.practicum.server.service.StatService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StatsController {
    private static final String DATE_MASK = "yyyy-MM-dd HH:mm:ss";

    private final StatService statService;

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStatsDto> get(@RequestParam(required = false) @DateTimeFormat(pattern = DATE_MASK) LocalDateTime start,
                                  @RequestParam(required = false) @DateTimeFormat(pattern = DATE_MASK) LocalDateTime end,
                                  @RequestParam(required = false) List<String> uris,
                                  @RequestParam(defaultValue = "false") Boolean unique) {
        if (start == null) {
            throw new BadRequestException("Не передан параметр start");
        }

        if (end == null) {
            throw new BadRequestException("Не передан параметр end");
        }

        if (start.isAfter(end)) {
            throw new BadRequestException("start > end");
        }
        log.info("GET /stats");
        return statService.get(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto save(@RequestBody EndpointHitDto dto) {
        log.info("POST /hit");
        return statService.save(dto);
    }
}
