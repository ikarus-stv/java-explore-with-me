package ru.practicum.ewm.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.admin.service.AdminCategoryService;
import ru.practicum.ewm.base.dto.CategoryDto;
import ru.practicum.ewm.base.dto.NewCategoryDto;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        log.info("POST /admin/categories : \"{}\"", newCategoryDto.getName());
        return adminCategoryService.save(newCategoryDto);
    }

    @PatchMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto update(@PathVariable Long categoryId,
                              @RequestBody @Valid NewCategoryDto request) {
        log.info("PATCH /admin/categories/{}", categoryId);

        CategoryDto result;

        result = adminCategoryService.update(request, categoryId);

        return result;
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long categoryId) {
        log.info("DELETE /admin/categories/{}", categoryId);
        adminCategoryService.delete(categoryId);
    }
}
