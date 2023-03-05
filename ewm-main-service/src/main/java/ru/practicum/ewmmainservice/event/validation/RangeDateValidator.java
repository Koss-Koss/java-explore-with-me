package ru.practicum.ewmmainservice.event.validation;

import lombok.experimental.UtilityClass;
import ru.practicum.ewmmainservice.exception.BadRequestException;

import java.time.LocalDateTime;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.RANGE_START_AFTER_RANGE_END_MESSAGE;

@UtilityClass
public class RangeDateValidator {
    public static void validateRangeStartNotAfterRangeEnd(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException(RANGE_START_AFTER_RANGE_END_MESSAGE);
        }
    }
}
