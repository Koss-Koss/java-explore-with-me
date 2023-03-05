package ru.practicum.ewmmainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmmainservice.config.CustomDateTimeDeserializer;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;
import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserEventDto {
    @Size(min = EVENT_ANNOTATION_LENGTH_MIN, max = EVENT_ANNOTATION_LENGTH_MAX,
            message = EVENT_ANNOTATION_SIZE_INCORRECT_MESSAGE)
    String annotation;
    @JsonProperty("category")
    Long categoryId;
    @Size(min = EVENT_DESCRIPTION_LENGTH_MIN, max = EVENT_DESCRIPTION_LENGTH_MAX,
            message = EVENT_DESCRIPTION_SIZE_INCORRECT_MESSAGE)
    String description;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    LocalDateTime eventDate;
    @JsonProperty("location")
    LocationDto locationDto;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    @JsonProperty("stateAction")
    @Enumerated(EnumType.STRING)
    EventState state;
    @Size(min = EVENT_TITLE_LENGTH_MIN, max = EVENT_TITLE_LENGTH_MAX,
            message = EVENT_TITLE_SIZE_INCORRECT_MESSAGE)
    String title;

    @Override
    public String toString() {
        return "{\"annotation\":\"" + annotation + "\"," +
                "\"description\":\"" + description + "\"," +
                "\"eventDate\":\"" + eventDate.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)) + "\"," +
                "\"paid\":" + paid + "," +
                "\"participantLimit\":" + participantLimit + "," +
                "\"requestModeration\":" + requestModeration + "," +
                "\"title\":\"" + title + "\"," +
                "\"stateAction\":\"" + state + "\"," +
                "\"category\":" + categoryId + "," +
                "\"location\":{\"lat\":" + locationDto.getLat() + ",\"lon\":"+ locationDto.getLon() + "}" +
                '}';
    }
}
