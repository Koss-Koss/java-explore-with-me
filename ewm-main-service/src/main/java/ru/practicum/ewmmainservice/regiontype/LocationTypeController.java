package ru.practicum.ewmmainservice.regiontype;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmainservice.regiontype.model.dto.RegionTypeDto;
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
public class LocationTypeController {
    private final RegionTypeService regionTypeService;

    @PostMapping(ADMIN_PATH + REGION_TYPE_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public RegionTypeDto createIsAdmin(@Valid @RequestBody RegionTypeDto regionTypeDto) {
        log.info("Получен запрос POST к эндпоинту: {}. Данные тела запроса: {}",
                ADMIN_PATH + REGION_TYPE_PATH, regionTypeDto);
        return regionTypeService.create(regionTypeDto);
    }

    @PatchMapping(ADMIN_PATH + REGION_TYPE_PATH + REGION_TYPE_PREFIX)
    public RegionTypeDto updateIsAdmin(@Valid @RequestBody RegionTypeDto regionTypeDto,
                                       @PathVariable long typeId) {
        log.info("Получен запрос PATCH к эндпоинту: {}/{}. Данные тела запроса: {}",
                ADMIN_PATH + REGION_TYPE_PATH, typeId, regionTypeDto);
        return regionTypeService.update(typeId, regionTypeDto);
    }

    @DeleteMapping(ADMIN_PATH + REGION_TYPE_PATH + REGION_TYPE_PREFIX)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIsAdmin(@PathVariable long typeId) {
        log.info("Получен запрос DELETE к эндпоинту: {}/{}", ADMIN_PATH + REGION_TYPE_PATH, typeId);
        regionTypeService.delete(typeId);
    }

    @GetMapping(ADMIN_PATH + REGION_TYPE_PATH)
    public Collection<RegionTypeDto> getAllIsAdmin(
            @PositiveOrZero(message = NEGATIVE_FROM_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_FROM_AS_STRING) int from,
            @Positive(message = NOT_POSITIVE_SIZE_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_SIZE_AS_STRING) int size) {
        log.info("Получен запрос GET к эндпоинту: {}. Параметры пагинации: from = {}, size = {}",
                ADMIN_PATH + REGION_TYPE_PATH, from, size);
        return regionTypeService.findAll(
                PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT)
        ).getContent();
    }

    @GetMapping(ADMIN_PATH + REGION_TYPE_PATH + REGION_TYPE_PREFIX)
    public RegionTypeDto getByIdIsAdmin(@PathVariable long typeId) {
        log.info("Получен запрос GET к эндпоинту: {}/{}", ADMIN_PATH + REGION_TYPE_PATH, typeId);
        return regionTypeService.findById(typeId);
    }
}
