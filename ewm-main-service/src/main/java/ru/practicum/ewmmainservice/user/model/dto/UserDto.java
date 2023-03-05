package ru.practicum.ewmmainservice.user.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;

@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
@ToString
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDto {
    Long id;
    @NotEmpty(message = USER_NOT_NAME_MESSAGE)
    String name;
    @NotEmpty(message = USER_NOT_EMAIL_MESSAGE)
    @Email(message = USER_EMAIL_INCORRECT_MESSAGE)
    String email;
}
