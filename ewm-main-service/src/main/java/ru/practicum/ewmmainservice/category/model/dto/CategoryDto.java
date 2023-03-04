package ru.practicum.ewmmainservice.category.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CategoryDto {
    Long id;
    @NotEmpty(message = "Не указано имя (name) категории")
    String name;
}
