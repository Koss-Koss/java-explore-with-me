package ru.practicum.ewmmainservice.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmainservice.compilation.model.dto.CompilationDto;
import ru.practicum.ewmmainservice.compilation.model.dto.NewCompilationDto;
import ru.practicum.ewmmainservice.compilation.model.dto.UpdateCompilationDto;
import ru.practicum.ewmmainservice.pagination.PaginationUtils;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;
import static ru.practicum.ewmmainservice.pagination.PaginationConstant.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class CompilationController {
    private final CompilationService compilationService;

    @PostMapping(ADMIN_PATH + COMPILATION_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createIsAdmin(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Получен запрос POST к эндпоинту: {}{}. Данные тела запроса: {}",
                ADMIN_PATH, COMPILATION_PATH, newCompilationDto);
        return compilationService.create(newCompilationDto);
    }

    @PatchMapping(value = ADMIN_PATH + COMPILATION_PATH + COMPILATION_PREFIX)
    public CompilationDto updateIsAdmin(@PathVariable long compId,
                                        @RequestBody UpdateCompilationDto updateCompilationDto) {
        log.info("Получен запрос PATCH к эндпоинту: {}{}/{}. Данные тела запроса: {}",
                ADMIN_PATH, COMPILATION_PATH, compId, updateCompilationDto);
        return compilationService.update(compId, updateCompilationDto);
    }

    @DeleteMapping(ADMIN_PATH + COMPILATION_PATH + COMPILATION_PREFIX)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIsAdmin(@PathVariable long compId) {
        log.info("Получен запрос DELETE к эндпоинту: {}{}/{}",
                ADMIN_PATH, COMPILATION_PATH, compId);
        compilationService.delete(compId);
    }

    @GetMapping(COMPILATION_PATH + COMPILATION_PREFIX)
    public CompilationDto getByIdIsPublic(@PathVariable long compId) {
        log.info("Получен запрос GET к эндпоинту: {}/{}",
                COMPILATION_PATH, compId);
        return compilationService.findById(compId);
    }

    @GetMapping(COMPILATION_PATH)
    public Collection<CompilationDto> getAllByPinnedIsPublic(
            @Valid @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero(message = NEGATIVE_FROM_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_FROM_AS_STRING) int from,
            @Positive(message = NOT_POSITIVE_SIZE_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_SIZE_AS_STRING) int size) {
        log.info("Получен запрос GET к эндпоинту: {}. Параметры пагинации: from = {}, size = {}",
                CATEGORY_PATH, from, size);
        return compilationService.findByPinned(
                pinned,
                PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT)
        ).getContent();
    }
}
