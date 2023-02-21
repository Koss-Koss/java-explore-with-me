package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.service.HitService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.stats.dto.StatsDtoConstants.DATE_FORMAT;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class HitController {

    private final HitService hitService;
    protected static final String HIT_PATH = "/hit";
    protected static final String STATS_PATH = "/stats";

    @PostMapping(value = HIT_PATH)
    public HitDto hit(@RequestBody @Valid HitDto hitDto) {
        log.info("Получен запрос POST к эндпоинту: {}. Данные тела запроса: {}",
                HIT_PATH, hitDto);
        return hitService.create(hitDto);
    }

    @GetMapping(value = STATS_PATH)
    public List<StatsDto> stats(
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
            @RequestParam String[] uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        log.info("Получен запрос GET к эндпоинту: {}",
                STATS_PATH);
        return hitService.findStats(start, end, uris, unique);
    }
}
