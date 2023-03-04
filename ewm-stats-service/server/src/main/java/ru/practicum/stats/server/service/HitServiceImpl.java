package ru.practicum.stats.server.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.model.Hit;
import ru.practicum.stats.server.model.HitMapper;
import ru.practicum.stats.server.model.Stats;
import ru.practicum.stats.server.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class HitServiceImpl implements HitService {

    private final HitRepository hitRepository;

    @Transactional
    @Override
    public HitDto create(HitDto hitDto) {
        Hit hit = HitMapper.toHit(hitDto);
        Hit result = hitRepository.save(hit);
        log.info("Создана запись в hits: {}", result);
        return hitDto;
    }

    @Override
    public List<StatsDto> findStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<Stats> result;
        if (!unique) {
            result = hitRepository.findAllNotUniqueIP(start, end, List.of(uris));
        } else {
            result = hitRepository.findAllUniqueIP(start, end, List.of(uris));
        }
        log.info("Получена выборка из DB: " + result.toString());
        return result.stream().map(HitMapper::toStatsDto).collect(Collectors.toList());
    }

}
