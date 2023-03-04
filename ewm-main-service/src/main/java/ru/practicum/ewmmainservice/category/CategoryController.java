package ru.practicum.ewmmainservice.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmainservice.category.model.dto.CategoryDto;
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
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping(ADMIN_PATH + CATEGORY_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createIsAdmin(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос POST к эндпоинту: {}. Данные тела запроса: {}",
                ADMIN_PATH + CATEGORY_PATH, categoryDto);
        return categoryService.create(categoryDto);
    }

    @PatchMapping(ADMIN_PATH + CATEGORY_PATH + CATEGORY_PREFIX)
    public CategoryDto updateIsAdmin(@Valid @RequestBody CategoryDto categoryDto,
                                     @PathVariable long catId) {
        log.info("Получен запрос PATCH к эндпоинту: {}/{}. Данные тела запроса: {}",
                ADMIN_PATH + CATEGORY_PATH, catId, categoryDto);
        return categoryService.update(catId, categoryDto);
    }

    @DeleteMapping(ADMIN_PATH + CATEGORY_PATH + CATEGORY_PREFIX)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIsAdmin(@PathVariable long catId) {
        log.info("Получен запрос DELETE к эндпоинту: {}/{}", ADMIN_PATH + CATEGORY_PATH, catId);
        categoryService.delete(catId);
    }

    @GetMapping(CATEGORY_PATH)
    public Collection<CategoryDto> getAllIsPublic(
            @PositiveOrZero(message = NEGATIVE_FROM_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_FROM_AS_STRING) int from,
            @Positive(message = NOT_POSITIVE_SIZE_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_SIZE_AS_STRING) int size) {
        log.info("Получен запрос GET к эндпоинту: {}. Параметры пагинации: from = {}, size = {}",
                CATEGORY_PATH, from, size);
        return categoryService.findAll(
                PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT)
        ).getContent();
    }

    @GetMapping(CATEGORY_PATH + CATEGORY_PREFIX)
    public CategoryDto getByIdIsPublic(@PathVariable long catId) {
        log.info("Получен запрос GET к эндпоинту: {}/{}", CATEGORY_PATH, catId);
        return categoryService.findById(catId);
    }
}
