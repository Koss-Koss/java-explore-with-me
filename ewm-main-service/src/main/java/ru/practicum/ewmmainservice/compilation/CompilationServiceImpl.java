package ru.practicum.ewmmainservice.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmainservice.compilation.model.Compilation;
import ru.practicum.ewmmainservice.compilation.model.dto.*;
import ru.practicum.ewmmainservice.exception.BadRequestException;
import ru.practicum.ewmmainservice.exception.NotFoundException;

import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.COMPILATION_ALREADY_EXISTS_MESSAGE;
import static ru.practicum.ewmmainservice.exception.errormessage.ErrorMessageConstants.COMPILATION_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        try {
            Compilation compilation = compilationRepository.save(compilationMapper.toCompilation(newCompilationDto));
            log.info("Добавлена подборка событий с id = {}", compilation.getId());
            return compilationMapper.toCompilationDto(compilation);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(COMPILATION_ALREADY_EXISTS_MESSAGE + newCompilationDto.getTitle());
        }
    }

    @Transactional
    @Override
    public CompilationDto update(long compilationId, UpdateCompilationDto compilationDto) {
        Compilation compilation = compilationRepository.extract(compilationId);
        Compilation updatedCompilation = compilationMapper.toCompilation(compilation, compilationDto);
        log.info("Изменена подборка событий с id = {}", compilationId);
        return compilationMapper.toCompilationDto(compilationRepository.save(updatedCompilation));
    }

    @Transactional
    @Override
    public void delete(long compilationId) {
        try {
            compilationRepository.deleteById(compilationId);
            log.info("Удалена подборка событий с id = {}", compilationId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(COMPILATION_NOT_FOUND_MESSAGE + compilationId);
        }
    }

    @Override
    public CompilationDto findById(long compilationId) {
        Compilation compilation = compilationRepository.extract(compilationId);
        return compilationMapper.toCompilationDto(compilation);
    }

    @Override
    public Page<CompilationDto> findByPinned(Boolean pinned, Pageable pageable) {
        if (pinned == null) {
            return compilationRepository.findAll(pageable).map(compilationMapper::toCompilationDto);
        }
        return compilationRepository.findAllByPinned(pinned, pageable).map(compilationMapper::toCompilationDto);
    }
}
