package ru.practicum.ewm.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.CategoryDto;
import ru.practicum.ewm.base.exceptions.DataNotFoundException;
import ru.practicum.ewm.base.mapper.CategoryMapper;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.repository.CategoryRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonCategoryService  {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    public Category findById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category not found, ID = " + catId));
    }

    public Collection<CategoryDto> getAll(Integer from, Integer size) {
        PageRequest request = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "id"));
        List<Category> list = categoryRepository.findAll(request).toList();
        return mapper.mapToListDto(list);
    }

    public CategoryDto get(Long catId) {
        return mapper.mapToDto(findById(catId));
    }
}
