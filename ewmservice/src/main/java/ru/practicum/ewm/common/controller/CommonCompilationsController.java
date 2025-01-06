package ru.practicum.ewm.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.CompilationDto;
import ru.practicum.ewm.common.service.CommonCompilationsService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/compilations")
public class CommonCompilationsController {

    private final CommonCompilationsService commonCompilationsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CompilationDto> get(@RequestParam(defaultValue = "false") Boolean pinned,
                                          @RequestParam(defaultValue = "0") Integer from,
                                          @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /compilations from = {} size = {}", from, size);
        return commonCompilationsService.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto get(@PathVariable Long compId) {
        log.info("Получен запрос GET /compilations/{}", compId);
        return commonCompilationsService.get(compId);
    }
}
