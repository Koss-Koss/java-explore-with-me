package ru.practicum.ewmmainservice.event.model.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.event.model.Event;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.exception.ConflictException;

import java.time.LocalDateTime;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;

@UtilityClass
public class UpdateEventMapper {
    public static Event toUpdateEventByUser(Event event, UpdateUserEventDto dto) {
        updateIdenticalFields(event, dto.getAnnotation(), dto.getDescription(), dto.getEventDate(),
                dto.getPaid(), dto.getParticipantLimit(), dto.getRequestModeration(), dto.getTitle());

        if (dto.getState() != null) {
            if (dto.getState().equals(EventState.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            } else if (event.getState().equals(EventState.PENDING) ||
                    event.getState().equals(EventState.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            } else {
                throw new ConflictException(NOT_CANCELED_EVENT_INITIATOR_MESSAGE);
            }
        }
        return event;
    }

    public static Event toUpdateEventByAdmin(Event event, UpdateAdminEventDto dto) {
        updateIdenticalFields(event, dto.getAnnotation(), dto.getDescription(), dto.getEventDate(),
                dto.getPaid(), dto.getParticipantLimit(), dto.getRequestModeration(), dto.getTitle());

        if (dto.getState() != null) {
            if (dto.getState().equals(EventState.PUBLISH_EVENT)) {
                if (event.getState().equals(EventState.PENDING)) {
                    event.setState(EventState.PUBLISHED);
                } else {
                    throw new ConflictException(NOT_PUBLISHED_EVENT_ADMIN_MESSAGE);
                }
            }
            if (dto.getState().equals(EventState.REJECT_EVENT)) {
                if (!event.getState().equals(EventState.PUBLISHED)) {
                    event.setState(EventState.CANCELED);
                } else {
                    throw new ConflictException(NOT_CANCELED_EVENT_ADMIN_MESSAGE);
                }
            }
        }
        return event;
    }

    private static void updateIdenticalFields(Event event, String annotation, String description,
                                              LocalDateTime eventDate, Boolean paid, Long participantLimit,
                                              Boolean requestModeration, String title) {
        if (annotation != null) {
            event.setAnnotation(annotation);
        }
        if (description != null) {
            event.setDescription(description);
        }
        if (eventDate != null) {
            event.setEventDate(eventDate);
        }
        if (paid != null) {
            event.setPaid(paid);
        }
        if (participantLimit != null) {
            event.setParticipantLimit(participantLimit);
        }
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
        if (title != null) {
            event.setTitle(title);
        }
    }
}
