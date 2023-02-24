package ru.practicum.ewmmainservice.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmainservice.pagination.PaginationUtils;
import ru.practicum.ewmmainservice.user.model.dto.UserDto;
import ru.practicum.ewmmainservice.user.model.dto.UserShortDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.COMMON_ADMIN_PATH;
import static ru.practicum.ewmmainservice.EwmMainServiceConstants.COMMON_USER_PATH;
import static ru.practicum.ewmmainservice.pagination.PaginationConstant.*;

@RestController
@RequestMapping(path = COMMON_ADMIN_PATH + COMMON_USER_PATH)
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    protected static final String USER_PREFIX = "/{userId}";

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info("Получен запрос POST к эндпоинту: {}. Данные тела запроса: {}",
                COMMON_ADMIN_PATH + COMMON_USER_PATH, userDto);
        return userService.create(userDto);
    }

    @DeleteMapping(USER_PREFIX)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId) {
        log.info("Получен запрос DELETE к эндпоинту: {}/{}", COMMON_ADMIN_PATH + COMMON_USER_PATH, userId);
        userService.delete(userId);
    }

    @GetMapping
    public Collection<UserDto> getAllUsers(
        @RequestParam(defaultValue = "") Collection<Long> ids,
        @PositiveOrZero(message = NEGATIVE_FROM_ERROR)
        @RequestParam(defaultValue = DEFAULT_PAGINATION_FROM_AS_STRING) int from,
        @Positive(message = NOT_POSITIVE_SIZE_ERROR)
        @RequestParam(defaultValue = DEFAULT_PAGINATION_SIZE_AS_STRING) int size) {
        log.info("Получен запрос GET к эндпоинту: {} для списка id пользователей {}. " +
                        "Параметры пагинации: from = {}, size = {}",
                COMMON_ADMIN_PATH + COMMON_USER_PATH, ids, from, size);
        return userService.findAllByIds(
                ids,
                PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT)
        ).getContent();
    }
}