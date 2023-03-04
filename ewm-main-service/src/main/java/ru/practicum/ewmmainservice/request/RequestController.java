package ru.practicum.ewmmainservice.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmainservice.request.model.dto.EventRequestStatusUpdateDto;
import ru.practicum.ewmmainservice.request.model.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.ewmmainservice.request.model.dto.ParticipationRequestDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @PostMapping(USER_PATH + USER_PREFIX + REQUEST_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createIsPrivate(@PathVariable long userId,
                                                   @RequestParam(required = true) long eventId) {
        log.info("Получен запрос POST к эндпоинту: {}/{}{}. Параметр запроса eventId: {}",
                USER_PATH, userId, REQUEST_PATH, eventId);
        return requestService.create(userId, eventId);
    }

    @PatchMapping(USER_PATH + USER_PREFIX + REQUEST_PATH + REQUEST_PREFIX + REQUEST_CANCEL_PATH)
    public ParticipationRequestDto cancelIsPrivate(@PathVariable long userId,
                                                   @PathVariable long requestId) {
        log.info("Получен запрос PATCH к эндпоинту: {}/{}{}/{}{}",
                USER_PATH, userId, REQUEST_PATH, requestId, REQUEST_CANCEL_PATH);
        return requestService.cancel(userId, requestId);
    }

    @PatchMapping(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX + REQUEST_PATH)
    public EventRequestStatusUpdateResultDto updateRequestsStatusIsPrivate(
            @PathVariable long userId,
            @PathVariable long eventId,
            @Valid @RequestBody(required = false) EventRequestStatusUpdateDto eventRequestStatusUpdateDto) {
        log.info("Получен запрос PATCH к эндпоинту: {}/{}{}/{}{}. Тело запроса: {}",
                USER_PATH, userId, EVENT_PATH, eventId, REQUEST_PATH,
                eventRequestStatusUpdateDto.toString());
        return requestService.updateRequestStatus(userId, eventId, eventRequestStatusUpdateDto);
    }

    @GetMapping(USER_PATH + USER_PREFIX + EVENT_PATH + EVENT_PREFIX + REQUEST_PATH)
    public List<ParticipationRequestDto> getAllByEventIdAndInitiatorIdIsPrivate(
            @PathVariable long userId, @PathVariable long eventId) {
        log.info("Получен запрос GET к эндпоинту: {}/{}{}/{}{}",
                USER_PATH, userId, EVENT_PATH, eventId, REQUEST_PATH);
        return requestService.findAllByEventIdAndInitiatorId(userId, eventId);
    }

    @GetMapping(USER_PATH + USER_PREFIX + REQUEST_PATH)
    public List<ParticipationRequestDto> getAllByRequesterIdIsPrivate(@PathVariable long userId) {
        log.info("Получен запрос GET к эндпоинту: {}/{}{}",
                USER_PATH, userId, REQUEST_PATH);
        return requestService.findAllByRequesterId(userId);
    }

}
