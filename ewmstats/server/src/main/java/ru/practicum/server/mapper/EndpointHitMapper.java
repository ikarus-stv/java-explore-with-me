package ru.practicum.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.server.model.EndpointHit;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class EndpointHitMapper {
    public abstract EndpointHitDto mapToDto(EndpointHit entity);

    public abstract EndpointHit mapToEntity(EndpointHitDto dto);
}
