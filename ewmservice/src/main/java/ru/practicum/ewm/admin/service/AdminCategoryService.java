package ru.practicum.ewm.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.CategoryDto;
import ru.practicum.ewm.base.dto.NewCategoryDto;
import ru.practicum.ewm.base.exceptions.IntegrityException;
import ru.practicum.ewm.base.exceptions.DataNotFoundException;
import ru.practicum.ewm.base.mapper.CategoryMapper;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.repository.CategoryRepository;
import ru.practicum.ewm.base.repository.EventRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCategoryService  {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final CategoryMapper mapper;

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("Категория " + categoryId + " не найдена"));
    }

    public CategoryDto save(NewCategoryDto newCategoryDto) {
        Category category = mapper.mapToEntity(newCategoryDto);
        category = categoryRepository.save(category);
        log.info("Save new category {}", newCategoryDto.getName());
        return mapper.mapToDto(category);
    }

    public CategoryDto update(NewCategoryDto request, Long catId) {
        Category updatedCategory = CategoryMapper.updateFields(findById(catId), request);
        updatedCategory = categoryRepository.save(updatedCategory);
        log.info("Update category {}", updatedCategory.getName());
        return mapper.mapToDto(updatedCategory);
    }

    public void delete(Long categoryId) {
        Category category = findById(categoryId);
        if (eventRepository.existsByCategory(category)) {
            throw new IntegrityException("Can't delete category " + category.getName());
        } else {
            categoryRepository.deleteById(categoryId);
            log.info("Category deleted: {}", category.getName());
        }
    }
}
