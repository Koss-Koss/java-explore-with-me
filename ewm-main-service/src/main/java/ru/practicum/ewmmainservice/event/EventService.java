package ru.practicum.ewmmainservice.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmmainservice.event.model.Event;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.event.model.dto.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Set;

public interface EventService {

    EventFullDto create(long userId, NewEventDto dto);

    EventFullDto updateIsPrivate(long userId, long eventId, UpdateUserEventDto dto);

    EventFullDto updateIsAdmin(long eventId, UpdateAdminEventDto dto);

    boolean existsCategoryRelatedEvents(long categoryId);

    Event getById(long id);

    Page<EventShortDto> findAllByInitiatorId(long userId, Pageable pageable);

    EventFullDto findByIdAndInitiatorId(long userId, long eventId);

    Page<EventFullDto> findAllByParams(Set<Long> users, Set<EventState> states, Set<Long> categories,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    Page<EventShortDto> findPublishedAllByParamsAndText(
            String text, Set<Long> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
            Pageable pageable, HttpServletRequest request);

    EventFullDto findPublishedById(long eventId, HttpServletRequest request);

    void changeConfirmedRequests(long id, long changeSize);

    void changeViews(Set<Long> ids);

    Page<EventFullDto> findAllInRegion(long regionId, Pageable pageable);
}
