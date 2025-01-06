package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.ViewStats;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ViewStatsMapper {
    public abstract ViewStatsDto mapToDto(ViewStats entity);

    public abstract ViewStats mapToEntity(ViewStatsDto dto);

    public abstract List<ViewStatsDto> mapToListDto(List<ViewStats> listViewStats);
}
