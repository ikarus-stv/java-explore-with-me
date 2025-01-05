package ru.practicum.ewm.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.admin.service.AdminCompilationService;
import ru.practicum.ewm.base.dto.CompilationDto;
import ru.practicum.ewm.base.dto.NewCompilationDto;
import ru.practicum.ewm.base.dto.UpdateCompilationRequest;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class AdminCompilationsController {

    private final AdminCompilationService adminCompilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto save(@RequestBody @Valid NewCompilationDto request) {
        log.info("POST /admin/compilations : {}", request.getTitle());
        return adminCompilationService.save(request);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable Long compId,
                                 @RequestBody @Valid UpdateCompilationRequest request) {
        log.info("PATCH /admin/compilations/{}", compId);
        return adminCompilationService.update(request, compId);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        log.info("DELETE /admin/compilations/{}", compId);
        adminCompilationService.delete(compId);
    }
}
