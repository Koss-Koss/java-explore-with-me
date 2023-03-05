package ru.practicum.ewmmainservice.request.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmmainservice.config.CustomDateTimeSerializer;
import ru.practicum.ewmmainservice.request.model.RequestStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {
    Long id;
    @JsonProperty(value = "event")
    Long eventId;
    @JsonProperty(value = "requester")
    Long requesterId;
    @Enumerated(EnumType.STRING)
    RequestStatus status;
    @JsonProperty(value = "created")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    LocalDateTime createdOn;
}
