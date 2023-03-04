package ru.practicum.ewmmainservice.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmainservice.category.CategoryRepository;
import ru.practicum.ewmmainservice.category.model.Category;
import ru.practicum.ewmmainservice.event.model.Event;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.event.model.dto.*;
import ru.practicum.ewmmainservice.event.validation.EventDateValidator;
import ru.practicum.ewmmainservice.exception.BadRequestException;
import ru.practicum.ewmmainservice.exception.ConflictException;
import ru.practicum.ewmmainservice.exception.NotFoundException;
import ru.practicum.ewmmainservice.location.LocationService;
import ru.practicum.ewmmainservice.location.model.Location;
import ru.practicum.ewmmainservice.user.UserRepository;
import ru.practicum.ewmmainservice.user.model.User;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.HitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.DATE_TIME_FORMAT;
import static ru.practicum.ewmmainservice.EwmMainServiceConstants.EWM_MAIN_SERVICE_APP_NAME;
import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final StatsClient statsClient;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationService locationService;
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional
    @Override
    public EventFullDto create(long userId, NewEventDto dto) {
        User user = userRepository.extract(userId);
        Category category = categoryRepository.extract(dto.getCategoryId());

        Location location = locationService.create(dto.getLocationDto());
        Event createdEvent = eventRepository.save(
                eventMapper.toEvent(eventMapper.toEventCreateDto(dto), user, category, location));
        log.info("Добавлено событие с id = {}", createdEvent.getId());
        return eventMapper.toEventFullDto(createdEvent);
    }

    @Transactional
    @Override
    public EventFullDto updateIsPrivate(long userId, long eventId, UpdateUserEventDto dto) {
        User user = userRepository.extract(userId);
        Event event = eventRepository.extract(eventId);

        if (!user.getId().equals(event.getInitiator().getId())) {
            log.error(USER_NOT_EVENT_INITIATOR_MESSAGE);
            throw new ConflictException(USER_NOT_EVENT_INITIATOR_MESSAGE);
        }

        if (!event.getState().equals(EventState.PENDING) && !event.getState().equals(EventState.CANCELED)) {
            throw new ConflictException(USER_NOT_EVENT_INITIATOR_MESSAGE);
        }

        if (dto.getLocationDto() != null) {
            locationService.update(event.getId(), dto.getLocationDto());
        }
        Event updatedEvent = eventRepository.save(UpdateEventMapper.toUpdateEventByUser(event, dto));
        log.info("Пользователем изменено событие с id = {}", updatedEvent.getId());
        return eventMapper.toEventFullDto(updatedEvent);
    }

    @Transactional
    @Override
    public EventFullDto updateIsAdmin(long eventId, UpdateAdminEventDto dto) {
        Event event = eventRepository.extract(eventId);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.extract(dto.getCategoryId());
            event.setCategory(category);
        }

        if (dto.getEventDate() != null) {
            EventDateValidator.validateEventDateEditable(dto.getEventDate(), event.getPublishedOn());
        }

        if (dto.getLocationDto() != null) {
            locationService.update(event.getId(), dto.getLocationDto());
        }
        Event updatedEvent = eventRepository.save(UpdateEventMapper.toUpdateEventByAdmin(event, dto));
        log.info("Администратором изменено событие с id = {}", updatedEvent.getId());
        return eventMapper.toEventFullDto(updatedEvent);
    }

    @Override
    public boolean existsCategoryRelatedEvents(long categoryId) {
        return eventRepository.findFirstByCategory_Id(categoryId).isPresent();
    }

    @Override
    public Event getById(long id) {
        return eventRepository.extract(id);
    }

    @Override
    public Page<EventShortDto> findAllByInitiatorId(long userId, Pageable pageable) {
        userRepository.extract(userId);
        return eventRepository.findAllByInitiatorId(userId, pageable).map(eventMapper::toEvenShortDto);
    }

    @Override
    public EventFullDto findByIdAndInitiatorId(long userId, long eventId) {
        User initiator = userRepository.extract(userId);
        Event event = eventRepository.extract(eventId);
        if (event.getInitiator().getId() != initiator.getId()) {
            throw new BadRequestException(USER_GET_NOT_EVENT_INITIATOR_MESSAGE + eventId);
        }
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public Page<EventFullDto> findAllByParams(Set<Long> users, Set<EventState> states, Set<Long> categories,
                                       LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        Page<Event> events = eventRepository.findAllByParams(users, states, categories, rangeStart, rangeEnd, pageable);
        return events.map(eventMapper::toEventFullDto);
    }

    @Transactional
    @Override
    public Page<EventShortDto> findPublishedAllByParamsAndText(
            String text, Set<Long> categories, Boolean paid,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
            Pageable pageable, HttpServletRequest request) {
        if (rangeStart == null & rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        Page<Event> events = eventRepository.findPublishedAllByParamsAndText(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, pageable);
        statsClient.createHit(makeHitDto(request));
        Collection<Event> eventCollection = events.getContent();
        Set<Long> ids = eventCollection.stream().map(Event::getId).collect(Collectors.toSet());
        changeViews(ids);
        return events.map(eventMapper::toEvenShortDto);
    }

    @Transactional
    @Override
    public EventFullDto findPublishedById(long eventId, HttpServletRequest request) {
        Event event = eventRepository.extract(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException(EVENT_NOT_AVAILABLE_FOR_VIEWING_MESSAGE + eventId);
        }
        statsClient.createHit(makeHitDto(request));
        changeViews(Set.of(eventId));
        return eventMapper.toEventFullDto(event);
    }

    @Transactional
    @Override
    public void changeConfirmedRequests(long id, long changeSize) {
        log.info("Для события с id = {} значение сonfirmedRequests изменено на {}", id, changeSize);
        eventRepository.changeConfirmedRequests(id, changeSize);
    }

    @Transactional
    @Override
    public void changeViews(Set<Long> ids) {
        log.info("Увеличено на единицу значение views для событий с id = {}", ids.toString());
        eventRepository.changeViews(ids);
    }

    private HitDto makeHitDto(HttpServletRequest request) {
        return new HitDto(
                EWM_MAIN_SERVICE_APP_NAME,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
        );
    }

}