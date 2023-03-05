package ru.practicum.ewmmainservice.locationtype;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmainservice.locationtype.model.dto.LocationTypeDto;
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
    private final LocationTypeService locationTypeService;

    @PostMapping(ADMIN_PATH + LOCATION_TYPE_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public LocationTypeDto createIsAdmin(@Valid @RequestBody LocationTypeDto locationTypeDto) {
        log.info("Получен запрос POST к эндпоинту: {}. Данные тела запроса: {}",
                ADMIN_PATH + LOCATION_TYPE_PATH, locationTypeDto);
        return locationTypeService.create(locationTypeDto);
    }

    @PatchMapping(ADMIN_PATH + LOCATION_TYPE_PATH + LOCATION_TYPE_PREFIX)
    public LocationTypeDto updateIsAdmin(@Valid @RequestBody LocationTypeDto locationTypeDto,
                                     @PathVariable long typeId) {
        log.info("Получен запрос PATCH к эндпоинту: {}/{}. Данные тела запроса: {}",
                ADMIN_PATH + LOCATION_TYPE_PATH, typeId, locationTypeDto);
        return locationTypeService.update(typeId, locationTypeDto);
    }

    @DeleteMapping(ADMIN_PATH + LOCATION_TYPE_PATH + LOCATION_TYPE_PREFIX)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIsAdmin(@PathVariable long typeId) {
        log.info("Получен запрос DELETE к эндпоинту: {}/{}", ADMIN_PATH + LOCATION_TYPE_PATH, typeId);
        locationTypeService.delete(typeId);
    }

    @GetMapping(ADMIN_PATH + LOCATION_TYPE_PATH)
    public Collection<LocationTypeDto> getAllIsAdmin(
            @PositiveOrZero(message = NEGATIVE_FROM_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_FROM_AS_STRING) int from,
            @Positive(message = NOT_POSITIVE_SIZE_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_SIZE_AS_STRING) int size) {
        log.info("Получен запрос GET к эндпоинту: {}. Параметры пагинации: from = {}, size = {}",
                ADMIN_PATH + LOCATION_TYPE_PATH, from, size);
        return locationTypeService.findAll(
                PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT)
        ).getContent();
    }

    @GetMapping(ADMIN_PATH + LOCATION_TYPE_PATH + LOCATION_TYPE_PREFIX)
    public LocationTypeDto getByIdIsAdmin(@PathVariable long typeId) {
        log.info("Получен запрос GET к эндпоинту: {}/{}", ADMIN_PATH + LOCATION_TYPE_PATH, typeId);
        return locationTypeService.findById(typeId);
    }
}
