package ru.practicum.ewmmainservice.user.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
@ToString
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    @NotEmpty(message = "Не указано имя (name) пользователя")
    String name;
    @NotEmpty(message = "Не указан email пользователя")
    @Email(message = "Указан некорректный email пользователя")
    String email;
}
