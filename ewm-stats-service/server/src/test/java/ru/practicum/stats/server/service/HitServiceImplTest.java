package ru.practicum.stats.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.model.Hit;
import ru.practicum.stats.server.model.HitMapper;
import ru.practicum.stats.server.model.Stats;
import ru.practicum.stats.server.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HitServiceImplTest {

    @Mock
    private HitRepository hitRepository;

    @InjectMocks
    private HitServiceImpl hitService;

    HitDto hitDto = HitDto.builder().app("app").uri("uri").ip("192.168.1.1").timestamp("2020-01-01 01:01:01").build();
    Hit hit = HitMapper.toHit(hitDto);
    StatsDto statsDto = StatsDto.builder().app(hitDto.getApp()).uri(hitDto.getUri()).hits(1).build();
    Stats stats = Stats.builder().app(hitDto.getApp()).uri(hitDto.getUri()).hits(1).build();
    String[] uris = new String[] { hitDto.getUri() };


    @Test
    void create_whenHitCreated_thenReturnedHitDto() {
        when(hitRepository.save(any(Hit.class))).thenReturn(hit);

        assertEquals(hitDto, hitService.create(hitDto));

        verify(hitRepository, times(1)).save(any(Hit.class));
    }

    @Test
    void findStats_whenUniqueFalseAndResultFound_thenReturnedListStatsDto() {
        when(hitRepository.findAllNotUniqueIP(any(LocalDateTime.class), any(LocalDateTime.class), anyList()))
                .thenReturn(Arrays.asList(stats));

        List<StatsDto> result = hitService.findStats(LocalDateTime.now(), LocalDateTime.now(), uris, false);

        assertEquals(Arrays.asList(statsDto).size(), result.size());
        assertEquals(Arrays.asList(statsDto).get(0), result.get(0));

        verify(hitRepository, times(1)).findAllNotUniqueIP(
                any(LocalDateTime.class), any(LocalDateTime.class), anyList());
    }

    @Test
    void findStats_whenUniqueTrueAndResultFound_thenReturnedListStatsDto() {
        when(hitRepository.findAllUniqueIP(any(LocalDateTime.class), any(LocalDateTime.class), anyList()))
                .thenReturn(Arrays.asList(stats));

        List<StatsDto> result = hitService.findStats(LocalDateTime.now(), LocalDateTime.now(), uris, true);

        assertEquals(Arrays.asList(statsDto).size(), result.size());
        assertEquals(Arrays.asList(statsDto).get(0), result.get(0));

        verify(hitRepository, times(1)).findAllUniqueIP(
                any(LocalDateTime.class), any(LocalDateTime.class), anyList());
    }

    @Test
    void findStats_whenUniqueFalseAndResultNotFound_thenReturnedEmpty() {
        when(hitRepository.findAllNotUniqueIP(any(LocalDateTime.class), any(LocalDateTime.class), anyList()))
                .thenReturn(Arrays.asList());

        List<StatsDto> result = hitService.findStats(LocalDateTime.now(), LocalDateTime.now(), uris, false);

        assertTrue(result.isEmpty());

        verify(hitRepository, times(1)).findAllNotUniqueIP(
                any(LocalDateTime.class), any(LocalDateTime.class), anyList());
    }

    @Test
    void findStats_whenUniqueTrueAndResultNotFound_thenReturnedEmpty() {
        when(hitRepository.findAllUniqueIP(any(LocalDateTime.class), any(LocalDateTime.class), anyList()))
                .thenReturn(Arrays.asList());

        List<StatsDto> result = hitService.findStats(LocalDateTime.now(), LocalDateTime.now(), uris, true);

        assertTrue(result.isEmpty());

        verify(hitRepository, times(1)).findAllUniqueIP(
                any(LocalDateTime.class), any(LocalDateTime.class), anyList());
    }
}