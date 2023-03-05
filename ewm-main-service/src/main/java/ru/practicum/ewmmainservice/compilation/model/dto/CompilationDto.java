package ru.practicum.ewmmainservice.compilation.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewmmainservice.event.model.dto.EventShortDto;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    Long id;
    Boolean pinned;
    String title;
    Set<EventShortDto> events;
}
