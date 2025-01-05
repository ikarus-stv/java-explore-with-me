package ru.practicum.ewm.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.NewUserRequest;
import ru.practicum.ewm.base.dto.UserDto;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.UserMapper;
import ru.practicum.ewm.base.model.User;
import ru.practicum.ewm.base.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper /*= Mappers.getMapper(UserMapper.class)*/;

    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User nit found " + userId));
    }

    public UserDto save(NewUserRequest request) {
        User user = userMapper.mapToEntity(request);
        user = userRepository.save(user);
        log.info("Save user {}", request.getName());
        return userMapper.mapToDto(user);
    }

    public Collection<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<User> users = (ids == null) ?
                userRepository.findAll(pageRequest).toList() : userRepository.findAllByIdIn(ids, pageRequest);

        log.info("Get user info {}", users.size());
        return userMapper.mapToListDto(users);
    }

    public void delete(Long userId) {
        User user = findById(userId);
        userRepository.deleteById(userId);
        log.info("User deleted {}", user.getName());
    }
}
