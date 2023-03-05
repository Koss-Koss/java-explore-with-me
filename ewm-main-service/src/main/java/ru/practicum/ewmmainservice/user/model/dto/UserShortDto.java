package ru.practicum.ewmmainservice.user.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
@ToString
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserShortDto {
    Long id;
    String name;
}
