package ru.practicum.ewm.base.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.practicum.ewm.base.dto.NewUserRequest;
import ru.practicum.ewm.base.dto.UserDto;
import ru.practicum.ewm.base.dto.UserShortDto;
import ru.practicum.ewm.base.model.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {
    public abstract User mapToEntity(NewUserRequest request);

    public abstract UserDto mapToDto(User entity);

    public abstract UserShortDto mapToShortDto(User entity);

    public abstract List<UserDto> mapToListDto(List<User> listEntity);
}
