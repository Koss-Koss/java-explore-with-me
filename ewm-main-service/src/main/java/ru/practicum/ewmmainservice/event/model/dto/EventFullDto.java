package ru.practicum.ewmmainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmmainservice.category.model.dto.CategoryDto;
import ru.practicum.ewmmainservice.config.CustomDateTimeSerializer;
import ru.practicum.ewmmainservice.event.model.EventState;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;
import ru.practicum.ewmmainservice.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventFullDto {
    Long id;
    String annotation;
    @JsonProperty("category")
    CategoryDto categoryDto;
    Long confirmedRequests;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    LocalDateTime createdOn;
    String description;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    LocalDateTime eventDate;
    UserShortDto initiator;
    @JsonProperty("location")
    LocationDto locationDto;
    Boolean paid;
    Long participantLimit;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    LocalDateTime publishedOn;
    Boolean requestModeration;
    EventState state;
    String title;
    Long views;
}
