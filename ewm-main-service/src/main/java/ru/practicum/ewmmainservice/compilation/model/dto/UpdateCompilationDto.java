package ru.practicum.ewmmainservice.compilation.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Size;
import java.util.List;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.COMPILATION_TITLE_LENGTH_MAX;
import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.COMPILATION_TITLE_SIZE_INCORRECT_MESSAGE;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationDto {
    @Size(max = COMPILATION_TITLE_LENGTH_MAX,
            message = COMPILATION_TITLE_SIZE_INCORRECT_MESSAGE)
    String title;
    Boolean pinned;
    List<Long> events;
}
