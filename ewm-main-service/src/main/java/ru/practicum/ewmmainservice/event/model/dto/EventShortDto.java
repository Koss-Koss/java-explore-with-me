package ru.practicum.ewmmainservice.event.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmmainservice.category.model.dto.CategoryDto;
import ru.practicum.ewmmainservice.config.CustomDateTimeDeserializer;
import ru.practicum.ewmmainservice.config.CustomDateTimeSerializer;
import ru.practicum.ewmmainservice.user.model.dto.UserShortDto;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventShortDto {
    Long id;
    String annotation;
    @JsonProperty("category")
    CategoryDto categoryDto;
    Long confirmedRequests;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    LocalDateTime eventDate;
    UserShortDto initiator;
    Boolean paid;
    String title;
    Long views;
}
