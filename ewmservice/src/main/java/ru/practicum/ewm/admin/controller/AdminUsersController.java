package ru.practicum.ewm.admin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.admin.service.AdminUserService;
import ru.practicum.ewm.base.dto.NewUserRequest;
import ru.practicum.ewm.base.dto.UserDto;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class AdminUsersController {
    private final AdminUserService adminUserService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto post(@RequestBody @Valid NewUserRequest newUserRequest) {
        log.info("POST /admin/users");
        return adminUserService.save(newUserRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> get(@RequestParam(required = false) List<Long> ids,
                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("GET /admin/users ID = {} from = {}, size = {}", ids, from, size);
        return adminUserService.getUsers(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        log.info("DELETE /admin/users/{}", userId);
        adminUserService.delete(userId);
    }
}
