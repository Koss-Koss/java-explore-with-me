package ru.practicum.ewmmainservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.event.model.dto.*;
import ru.practicum.ewmmainservice.event.validation.EventDateValidator;
import ru.practicum.ewmmainservice.event.validation.RangeDateValidator;
import ru.practicum.ewmmainservice.exception.BadRequestException;
import ru.practicum.ewmmainservice.pagination.PaginationUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;
import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.INCORRECT_SORT_PARAM_MESSAGE;
import static ru.practicum.ewmmainservice.pagination.PaginationConstant.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {

    private final EventService eventService;

    @PostMapping(USER_PATH + USER_PREFIX + EVENT_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createIsPrivate(@Valid @RequestBody NewEventDto newEventDto, @PathVariable Long userId) {
        log.info("Получен запрос POST к эндпоинту: {}/{}{}. Данные тела запроса: {}",
                USER_PATH, userId, EVENT_PATH, newEventDto);
        /*Отключено, так как тесты postman допускают неправильные координаты
        LocationValidator.validate(newEventDto.getLocationDto());*/
        EventDateValidator.validateEventDateCreated(newEventDto.getEventDate());
        return eventService.create(userId, newEventDto);
    }

    @PatchMapping(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX)
    public EventFullDto updateIsPrivate(@Valid @RequestBody UpdateUserEventDto updateUserEventDto,
                                        @PathVariable Long userId,
                                        @PathVariable Long eventId) {
        log.info("Получен запрос PATCH к эндпоинту: {}/{}{}/{}. Данные тела запроса: {}",
                USER_PATH, userId, EVENT_PATH, eventId, updateUserEventDto);
        /*Отключено, так как тесты postman допускают неправильные координаты
        if(updateUserEventDto.getLocationDto() != null) {
            LocationValidator.validate(updateUserEventDto.getLocationDto());
        }*/
        if (updateUserEventDto.getEventDate() != null) {
            EventDateValidator.validateEventDateCreated(updateUserEventDto.getEventDate());
        }
        return eventService.updateIsPrivate(userId,eventId, updateUserEventDto);
    }

    @PatchMapping(ADMIN_PATH + EVENT_PATH + EVENT_PREFIX)
    public EventFullDto updateIsAdmin(@Valid @RequestBody UpdateAdminEventDto updateAdminEventDto,
                                      @PathVariable Long eventId) {
        log.info("Получен запрос PATCH к эндпоинту: {}/{}{}. Данные тела запроса: {}",
                ADMIN_PATH, EVENT_PATH, eventId, updateAdminEventDto);
        return eventService.updateIsAdmin(eventId, updateAdminEventDto);
    }

    @GetMapping(USER_PATH + USER_PREFIX + EVENT_PATH)
    public Collection<EventShortDto> getAllByInitiatorIdIsPrivate(
            @PathVariable Long userId,
            @PositiveOrZero(message = NEGATIVE_FROM_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_FROM_AS_STRING) int from,
            @Positive(message = NOT_POSITIVE_SIZE_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_SIZE_AS_STRING) int size) {
        log.info("Получен запрос GET к эндпоинту: {}/{}{} для списка событий, добавленных пользователем с id = {}. " +
                        "Параметры пагинации: from = {}, size = {}",
                USER_PATH, userId, EVENT_PATH, userId, from, size);
        return eventService.findAllByInitiatorId(
                userId,
                PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT)
        ).getContent();
    }

    @GetMapping(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX)
    public EventFullDto getByIdAndInitiatorIdIsPrivate(@PathVariable long userId, @PathVariable long eventId) {
        log.info("Получен запрос GET к эндпоинту: {}/{}{}/{}", USER_PATH, userId, EVENT_PATH, eventId);
        return eventService.findByIdAndInitiatorId(userId, eventId);
    }

    @GetMapping(ADMIN_PATH + EVENT_PATH)
    public Collection<EventFullDto> getAllByParamsIsAdmin(
            @RequestParam(required = false) Set<Long> users,
            @RequestParam(required = false) Set<EventState> states,
            @RequestParam(required = false) Set<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @PositiveOrZero(message = NEGATIVE_FROM_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_FROM_AS_STRING) int from,
            @Positive(message = NOT_POSITIVE_SIZE_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_SIZE_AS_STRING) int size) {
        if (rangeStart != null & rangeEnd != null) {
            RangeDateValidator.validateRangeStartNotAfterRangeEnd(rangeStart, rangeEnd);
        }
        log.info("Получен запрос GET к эндпоинту: {}{} для списка событий, добавленных пользователем с id = {}. " +
                        "Параметры поиска: users = {}, state = {}, categories = {}, rangeStart = {}, rangeEnd = {}. " +
                        "Параметры пагинации: from = {}, size = {}",
                ADMIN_PATH, EVENT_PATH, users, users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.findAllByParams(users, states, categories, rangeStart, rangeEnd,
                PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT)
                ).getContent();
    }

    @GetMapping(EVENT_PATH)
    public Collection<EventShortDto> getPublishedAllByParamsAndTextIsPublic(
            @RequestParam(required = false, defaultValue = "") String text,
            @RequestParam(required = false) Set<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = DATE_TIME_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @PositiveOrZero(message = NEGATIVE_FROM_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_FROM_AS_STRING) int from,
            @Positive(message = NOT_POSITIVE_SIZE_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_SIZE_AS_STRING) int size,
            HttpServletRequest request) {
        if (rangeStart != null & rangeEnd != null) {
            RangeDateValidator.validateRangeStartNotAfterRangeEnd(rangeStart, rangeEnd);
        }
        Pageable pageable;
        if (sort == null) {
            pageable = PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT);
        } else if (sort.equals(SORT_PARAM_EVENT_DATE_FOR_EVENT_SEARCH)) {
            pageable = PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, Sort.by("eventDate"));
        } else if (sort.equals(SORT_PARAM_VIEWS_FOR_EVENT_SEARCH)) {
            pageable = PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, Sort.by("views"));
        } else {
            throw new BadRequestException(INCORRECT_SORT_PARAM_MESSAGE + sort);
        }
        log.info("Получен запрос GET к эндпоинту: {} для списка событий. Параметры поиска: " +
                        "text = {}, categories = {}, paid = {}, rangeStart = {}, rangeEnd = {}, onlyAvailable = {}. " +
                        "Параметры пагинации: from = {}, size = {}",
                EVENT_PATH, text, categories, paid, rangeStart, rangeEnd, onlyAvailable, from, size);
        return eventService.findPublishedAllByParamsAndText(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable, request
        ).getContent();
    }

    @GetMapping(EVENT_PATH + EVENT_PREFIX)
    public EventFullDto getPublishedByIdIsPublic(@PathVariable long eventId, HttpServletRequest request) {
        log.info("Получен запрос GET к эндпоинту: {}/{}", EVENT_PATH, eventId);
        return eventService.findPublishedById(eventId, request);
    }
}
