package ru.practicum.ewmmainservice.location.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
@ToString
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LocationDto {
    Double lat;
    Double lon;
}
