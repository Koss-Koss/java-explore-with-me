package ru.practicum.ewmmainservice.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmainservice.category.model.dto.CategoryDto;
import ru.practicum.ewmmainservice.pagination.PaginationUtils;
import ru.practicum.ewmmainservice.user.model.dto.UserDto;

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
    protected static final String CATEGORY_PREFIX = "/{catId}";

    @PostMapping(COMMON_ADMIN_PATH + COMMON_CATEGORY_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Получен запрос POST к эндпоинту: {}. Данные тела запроса: {}",
                COMMON_ADMIN_PATH + COMMON_CATEGORY_PATH, categoryDto);
        return categoryService.create(categoryDto);
    }

    @PatchMapping(COMMON_ADMIN_PATH + COMMON_CATEGORY_PATH + CATEGORY_PREFIX)
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto,
                              @PathVariable long catId) {
        log.info("Получен запрос PATCH к эндпоинту: {}/{}. Данные тела запроса: {}",
                COMMON_ADMIN_PATH + COMMON_CATEGORY_PATH, catId, categoryDto);
        return categoryService.update(catId, categoryDto);
    }

    @DeleteMapping(COMMON_ADMIN_PATH + COMMON_CATEGORY_PATH + CATEGORY_PREFIX)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long catId) {
        log.info("Получен запрос DELETE к эндпоинту: {}/{}", COMMON_ADMIN_PATH + COMMON_CATEGORY_PATH, catId);
        categoryService.delete(catId);
    }

    @GetMapping(COMMON_CATEGORY_PATH)
    public Collection<CategoryDto> getAllCategories(
            @PositiveOrZero(message = NEGATIVE_FROM_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_FROM_AS_STRING) int from,
            @Positive(message = NOT_POSITIVE_SIZE_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_SIZE_AS_STRING) int size) {
        log.info("Получен запрос GET к эндпоинту: {}. Параметры пагинации: from = {}, size = {}",
                COMMON_CATEGORY_PATH, from, size);
        return categoryService.findAll(
                PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT)
        ).getContent();
    }

    @GetMapping(COMMON_CATEGORY_PATH + CATEGORY_PREFIX)
    public CategoryDto getCategoryById(@PathVariable long catId) {
        log.info("Получен запрос GET к эндпоинту: {}/{}", COMMON_CATEGORY_PATH, catId);
        return categoryService.findById(catId);
    }
}
