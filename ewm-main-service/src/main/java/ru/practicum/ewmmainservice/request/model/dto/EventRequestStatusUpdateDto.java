package ru.practicum.ewmmainservice.request.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmmainservice.request.model.RequestStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateDto {
    List<Long> requestIds;
    @Enumerated(EnumType.STRING)
    RequestStatus status;
}
