package ru.practicum.ewmmainservice.compilation.model.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmmainservice.compilation.model.Compilation;
import ru.practicum.ewmmainservice.event.EventService;
import ru.practicum.ewmmainservice.event.model.dto.EventMapper;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CompilationMapper {

    private EventService eventService;
    private EventMapper eventMapper;

    public Compilation toCompilation(NewCompilationDto dto) {
        return Compilation.builder()
                .id(null)
                .pinned(dto.getPinned())
                .title(dto.getTitle())
                .events(dto.getEvents()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(eventService::getById)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents()
                        .stream()
                        .map(eventMapper::toEvenShortDto)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public Compilation toCompilation(Compilation compilation, UpdateCompilationDto dto) {
        return Compilation.builder()
                .id(compilation.getId())
                .title(dto.getTitle() == null ? compilation.getTitle() : dto.getTitle())
                .pinned(dto.getPinned() == null ? compilation.getPinned() : dto.getPinned())
                .events(dto.getEvents() == null ? compilation.getEvents() : dto.getEvents()
                        .stream()
                        .filter(Objects::nonNull)
                        .map(eventService::getById)
                        .collect(Collectors.toSet())
                )
                .build();
    }

}
