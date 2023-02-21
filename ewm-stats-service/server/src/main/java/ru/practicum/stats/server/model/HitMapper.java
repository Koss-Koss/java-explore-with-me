package ru.practicum.stats.server.model;

import lombok.experimental.UtilityClass;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.practicum.stats.dto.StatsDtoConstants.DATE_FORMAT;

@UtilityClass
public class HitMapper {
    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(LocalDateTime.parse(hitDto.getTimestamp(), DateTimeFormatter.ofPattern(DATE_FORMAT)))
                .build();
    }

    public static StatsDto toStatsDto(Stats stats) {
        return StatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits())
                .build();
    }

}
