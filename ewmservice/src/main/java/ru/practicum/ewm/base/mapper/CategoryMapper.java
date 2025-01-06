package ru.practicum.ewm.base.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.ewm.base.dto.CategoryDto;
import ru.practicum.ewm.base.dto.NewCategoryDto;
import ru.practicum.ewm.base.model.Category;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class CategoryMapper {
    public abstract Category mapToEntity(NewCategoryDto dto);

    public abstract CategoryDto mapToDto(Category entity);

    public abstract  List<CategoryDto> mapToListDto(List<Category> listEntity);

    public static Category updateFields(Category entity, NewCategoryDto request) {
        entity.setName(request.getName());
        return entity;
    }

}
