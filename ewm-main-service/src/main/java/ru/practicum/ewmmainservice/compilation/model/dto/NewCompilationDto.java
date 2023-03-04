package ru.practicum.ewmmainservice.compilation.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;
import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    @NotBlank(message = COMPILATION_TITLE_NOT_BLANK_MESSAGE)
    @Size(max = COMPILATION_TITLE_LENGTH_MAX,
            message = COMPILATION_TITLE_SIZE_INCORRECT_MESSAGE)
    String title;
    Boolean pinned;
    List<Long> events;
}
