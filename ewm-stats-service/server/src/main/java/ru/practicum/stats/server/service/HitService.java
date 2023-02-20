package ru.practicum.stats.server.service;

import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {
    HitDto create(HitDto hitDto);

    List<StatsDto> findStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
