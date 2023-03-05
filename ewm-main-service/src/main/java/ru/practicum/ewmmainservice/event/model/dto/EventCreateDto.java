package ru.practicum.ewmmainservice.event.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventCreateDto {
    String annotation;
    Long categoryId;
    Long confirmedRequest;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    Long initiatorId;
    LocationDto locationDto;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    String title;
}
