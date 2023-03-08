package ru.practicum.ewmmainservice.region;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmainservice.pagination.PaginationUtils;
import ru.practicum.ewmmainservice.region.model.dto.NewRegionDto;
import ru.practicum.ewmmainservice.region.model.dto.RegionDto;
import ru.practicum.ewmmainservice.region.model.dto.UpdateRegionDto;
import ru.practicum.ewmmainservice.region.validation.RegionValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.Collection;

import static ru.practicum.ewmmainservice.EwmMainServiceConstants.*;
import static ru.practicum.ewmmainservice.pagination.PaginationConstant.*;

@RestController
@RequestMapping(path = ADMIN_PATH + REGION_PATH)
@RequiredArgsConstructor
@Slf4j
@Validated
public class RegionController {

    private final RegionService regionService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public RegionDto createIsAdmin(@Valid @RequestBody NewRegionDto newRegionDto,
                                   HttpServletRequest request) {
        log.info("Получен запрос POST к эндпоинту: {}. Данные тела запроса: {}",
                ADMIN_PATH + REGION_PATH, newRegionDto);
        RegionValidator.validate(
                newRegionDto.getLat(), newRegionDto.getLon(), newRegionDto.getRadius(), request.getMethod());
        return regionService.create(newRegionDto);
    }

    @PatchMapping(REGION_PREFIX)
    public RegionDto updateIsAdmin(@Valid @RequestBody UpdateRegionDto updateRegionDto,
                                   @PathVariable long regionId,
                                   HttpServletRequest request) {
        log.info("Получен запрос PATCH к эндпоинту: {}/{}. Данные тела запроса: {}",
                ADMIN_PATH + REGION_PATH, regionId, updateRegionDto);
        RegionValidator.validate(
                updateRegionDto.getLat(), updateRegionDto.getLon(), updateRegionDto.getRadius(), request.getMethod());
        return regionService.update(regionId, updateRegionDto);
    }

    @DeleteMapping(REGION_PREFIX)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIsAdmin(@PathVariable long regionId) {
        log.info("Получен запрос DELETE к эндпоинту: {}/{}", ADMIN_PATH + REGION_PATH, regionId);
        regionService.delete(regionId);
    }

    @GetMapping()
    public Collection<RegionDto> getAllByRegionType(
            @RequestParam(required = false) Long regionTypeId,
            @PositiveOrZero(message = NEGATIVE_FROM_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_FROM_AS_STRING) int from,
            @Positive(message = NOT_POSITIVE_SIZE_ERROR)
            @RequestParam(defaultValue = DEFAULT_PAGINATION_SIZE_AS_STRING) int size) {
        log.info("Получен запрос GET к эндпоинту: {}. Параметр regionTypeId = {}. Параметры пагинации: " +
                        "from = {}, size = {}", ADMIN_PATH + REGION_PATH, regionTypeId, from, size);
        return regionService.findAllByRegionType(
                regionTypeId,
                PageRequest.of(PaginationUtils.getCalculatedPage(from, size), size, DEFAULT_PAGINATION_SORT)
        ).getContent();
    }

    @GetMapping(REGION_PREFIX)
    public RegionDto getByIdIsAdmin(@PathVariable long regionId) {
        log.info("Получен запрос GET к эндпоинту: {}/{}", ADMIN_PATH + REGION_PATH, regionId);
        return regionService.findById(regionId);
    }
}
