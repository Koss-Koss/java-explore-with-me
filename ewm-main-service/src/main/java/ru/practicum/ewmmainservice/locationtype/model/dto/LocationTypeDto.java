package ru.practicum.ewmmainservice.locationtype.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LocationTypeDto {
    Long id;
    @NotEmpty(message = "Не указано имя (name) типа локации")
    String name;
}
