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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.DATE_TIME_FORMAT;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAdminEventDto {
    String annotation;
    @JsonProperty("category")
    Long categoryId;
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
