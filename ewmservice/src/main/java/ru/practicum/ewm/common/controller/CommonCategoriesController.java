package ru.practicum.ewm.common.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.base.dto.CategoryDto;
import ru.practicum.ewm.common.service.CommonCategoryService;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CommonCategoriesController {

    private final CommonCategoryService commonCategoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CategoryDto> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                          @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("GET /categories  from = {}, size = {}", from, size);
        return commonCategoryService.getAll(from, size);
    }

    @GetMapping("/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto get(@PathVariable Long catId) {
        log.info("GET /categories/{}", catId);
        return commonCategoryService.get(catId);
    }
}
