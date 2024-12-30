package ru.practicum.ewm.admin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.CategoryDto;
import ru.practicum.ewm.base.dto.NewCategoryDto;
import ru.practicum.ewm.base.exceptions.ConditionsNotMetException;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.CategoryMapper;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.repository.CategoryRepository;
import ru.practicum.ewm.base.repository.EventRepository;

@Slf4j
@Service
public class AdminCategoryService  {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;


    @Autowired
    public AdminCategoryService(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория c ID %d не найдена", categoryId)));
    }

    public CategoryDto save(NewCategoryDto request) {
        Category category = CategoryMapper.mapToEntity(request);
        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(String.format("Категория \"%s\" уже существует", category.getName()), e);
        }
        log.info("Сохраняем данные о категории {}", request.getName());
        return CategoryMapper.mapToDto(category);
    }

    public CategoryDto update(NewCategoryDto request, Long catId) {
        Category updatedCategory = CategoryMapper.updateFields(findById(catId), request);

        updatedCategory = categoryRepository.save(updatedCategory);

        log.info("Обновляем категорию \"{}\"", updatedCategory.getName());
        return CategoryMapper.mapToDto(updatedCategory);
    }

    public void delete(Long categoryId) {
        Category category = findById(categoryId);
        if (eventRepository.existsByCategory(category)) {
            throw new ConditionsNotMetException(String.format("Существуют события связанные с категорией %s, " +
                    "удаление категории невозможно!", category.getName()));
        } else {
            categoryRepository.deleteById(categoryId);
            log.info("Категория \"{}\" удалена", category.getName());
        }
    }
}
