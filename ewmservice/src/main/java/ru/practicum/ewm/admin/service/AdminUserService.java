package ru.practicum.ewm.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.NewUserRequest;
import ru.practicum.ewm.base.dto.UserDto;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.UserMapper;
import ru.practicum.ewm.base.model.User;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;

    private User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь c ID %d не найден", userId)));
    }

    public UserDto save(NewUserRequest request) {
        User user = UserMapper.mapToEntity(request);
//        try {
            user = userRepository.save(user);
//        } catch (DataIntegrityViolationException e) {
//            throw new ConflictException(String.format("Email %s уже занят", user.getEmail()), e);
//        }
        log.info("Сохраняем данные о пользователе {}", request.getName());
        return UserMapper.mapToDto(user);
    }

    public Collection<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        List<User> users;

        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        if (ids == null) {
            users = userRepository.findAll(pageRequest).toList();
        } else {
            users = userRepository.findAllByIdIn(ids, pageRequest);
        }

        log.info("Получаем данные о {} пользователях", users.size());
        return UserMapper.mapToListDto(users);
    }

    public void delete(Long userId) {
        User user = findById(userId);
        userRepository.deleteById(userId);
        log.info("Пользователь {} удален", user.getName());
    }
}
