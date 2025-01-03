package ru.practicum.ewm.common.service.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.CategoryDto;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.CategoryMapper;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.repository.CategoryRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class CommonCategoryService  {
    private final CategoryRepository categoryRepository;

    public CommonCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Категория c ID %d не найдена", catId)));
    }

    public Collection<CategoryDto> getAll(Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "id"));

        List<Category> test = categoryRepository.findAll(pageRequest).toList();

        return CategoryMapper.mapToListDto(test);
    }

    public CategoryDto get(Long catId) {
        return CategoryMapper.mapToDto(findById(catId));
    }
}
