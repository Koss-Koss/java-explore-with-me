package ru.practicum.ewmmainservice.compilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmmainservice.compilation.model.dto.CompilationDto;
import ru.practicum.ewmmainservice.compilation.model.dto.NewCompilationDto;
import ru.practicum.ewmmainservice.compilation.model.dto.UpdateCompilationDto;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);
    CompilationDto update(long compilationId, UpdateCompilationDto compilationDto);
    void delete(long compilationId);
    CompilationDto findById(long compilationId);

    Page<CompilationDto> findByPinned(Boolean pinned, Pageable pageable);
}
