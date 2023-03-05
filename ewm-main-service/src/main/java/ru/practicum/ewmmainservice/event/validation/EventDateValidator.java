package ru.practicum.ewmmainservice.event.validation;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.exception.ConflictException;

import java.time.LocalDateTime;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.LIMIT_DATE_EVENT_CHANGE_IN_FUTURE_IN_HOURS;
import static ru.practicum.ewmmainservice.EwmMainServiceConstants.LIMIT_DATE_EVENT_CREATION_IN_FUTURE_IN_HOURS;
import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;

@UtilityClass
public class EventDateValidator {
    public static void validateEventDateCreated(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(LIMIT_DATE_EVENT_CREATION_IN_FUTURE_IN_HOURS))) {
            throw new ConflictException(INCORRECT_EVENT_DATE_CREATED_MESSAGE);
        }
    }

    public static void validateEventDateEditable(LocalDateTime eventDate, LocalDateTime publishedOn) {
        if (eventDate.isBefore(LocalDateTime.now())) {
            throw new ConflictException(INCORRECT_EVENT_DATE_CHANGED_NOW_BEFORE_MESSAGE);
        }
        if (publishedOn != null &&
                eventDate.isBefore(publishedOn.plusHours(LIMIT_DATE_EVENT_CHANGE_IN_FUTURE_IN_HOURS))) {
            throw new ConflictException(INCORRECT_EVENT_DATE_CHANGED_MESSAGE);
        }
    }
}
