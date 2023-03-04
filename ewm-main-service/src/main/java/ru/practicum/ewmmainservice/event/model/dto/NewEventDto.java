package ru.practicum.ewmmainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmmainservice.config.CustomDateTimeDeserializer;
import ru.practicum.ewmmainservice.location.model.dto.LocationDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

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
public class NewEventDto {
    @NotNull
    @Size(min = EVENT_ANNOTATION_LENGTH_MIN, max = EVENT_ANNOTATION_LENGTH_MAX,
            message = EVENT_ANNOTATION_SIZE_INCORRECT_MESSAGE)
    String annotation;
    @JsonProperty("category")
    Long categoryId;
    @NotNull
    @Size(min = EVENT_DESCRIPTION_LENGTH_MIN, max = EVENT_DESCRIPTION_LENGTH_MAX,
            message = EVENT_DESCRIPTION_SIZE_INCORRECT_MESSAGE)
    String description;
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @NotNull
    LocalDateTime eventDate;
    @JsonProperty("location")
    LocationDto locationDto;
    Boolean paid;
    Long participantLimit;
    Boolean requestModeration;
    @NotNull
    @Size(min = EVENT_TITLE_LENGTH_MIN, max = EVENT_TITLE_LENGTH_MAX,
            message = EVENT_TITLE_SIZE_INCORRECT_MESSAGE)
    String title;
}
