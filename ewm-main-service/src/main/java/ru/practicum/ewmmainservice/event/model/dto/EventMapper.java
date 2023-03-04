package ru.practicum.ewmmainservice.event.model.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmmainservice.category.model.Category;
import ru.practicum.ewmmainservice.category.model.dto.CategoryMapper;
import ru.practicum.ewmmainservice.event.model.Event;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.location.model.Location;
import ru.practicum.ewmmainservice.location.model.dto.LocationMapper;
import ru.practicum.ewmmainservice.user.model.User;
import ru.practicum.ewmmainservice.user.model.dto.UserMapper;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class EventMapper {
    public EventCreateDto toEventCreateDto(NewEventDto newEventDto) {
        if (newEventDto == null) return null;
        return EventCreateDto.builder()
                .annotation(newEventDto.getAnnotation())
                .categoryId(newEventDto.getCategoryId())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .locationDto(newEventDto.getLocationDto())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public Event toEvent(EventCreateDto eventCreateDto, User user, Category category, Location location) {
        if (null == eventCreateDto) return null;
        return Event.builder()
                .id(null)
                .annotation(eventCreateDto.getAnnotation())
                .category(category)
                .confirmedRequests(0L)
                .createdOn(LocalDateTime.now())
                .description(eventCreateDto.getDescription())
                .eventDate(eventCreateDto.getEventDate())
                .initiator(user)
                .location(location)
                .paid(eventCreateDto.getPaid())
                .participantLimit(eventCreateDto.getParticipantLimit())
                .requestModeration(eventCreateDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(eventCreateDto.getTitle())
                .views(0L)
                .build();
    }

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .categoryDto(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .locationDto(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public EventShortDto toEvenShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .categoryDto(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}
