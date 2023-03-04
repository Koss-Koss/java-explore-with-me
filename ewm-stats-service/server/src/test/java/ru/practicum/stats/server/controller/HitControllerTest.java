package ru.practicum.stats.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatsDto;
import ru.practicum.stats.server.service.HitService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static ru.practicum.stats.dto.StatsDtoConstants.DATE_FORMAT;
import static ru.practicum.stats.server.controller.HitController.HIT_PATH;
import static ru.practicum.stats.server.controller.HitController.STATS_PATH;

@WebMvcTest(controllers = HitController.class)
class HitControllerTest {

    @MockBean
    private HitService hitService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    HitDto hitDto = HitDto.builder().app("app").uri("uri").ip("192.168.1.1").timestamp("2020-01-01 01:01:01").build();
    StatsDto statsDto = StatsDto.builder().app(hitDto.getApp()).uri(hitDto.getUri()).hits(1).build();

    @Test
    void hit_whenValidHitDto_thenResponseStatusOkWithHitDtoInBody() throws Exception {
        when(hitService.create(any(HitDto.class)))
                .thenReturn(hitDto);

        mvc.perform(post(HIT_PATH)
                        .content(mapper.writeValueAsString(hitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(mapper.writeValueAsString(hitDto)));
        verify(hitService, times(1)).create(any(HitDto.class));
    }

    @Test
    void hit_whenInvalidHitDto_thenResponseStatusBadRequest() throws Exception {
        when(hitService.create(any(HitDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mvc.perform(post(HIT_PATH)
                        .content(mapper.writeValueAsString(hitDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(hitService, times(1)).create(any(HitDto.class));
    }

    @Test
    void stats_whenValidAllParams_thenResponseStatusOkWithStatsDtoListInBody() throws Exception {
        final String start = DateTimeFormatter.ofPattern(DATE_FORMAT).format(LocalDateTime.now());
        final String end = DateTimeFormatter.ofPattern(DATE_FORMAT).format(LocalDateTime.now().plusMinutes(10));
        final List<String> uris = List.of("uri");
        final boolean unique = true;

        when(hitService.findStats(any(LocalDateTime.class), any(LocalDateTime.class),
                            any(String[].class), anyBoolean()))
                .thenReturn(Collections.singletonList(statsDto));

        mvc.perform(get(STATS_PATH)
                        .param("start", start)
                        .param("end", end)
                        .param("uris", uris.toArray(new String[0]))
                        .param("unique", Boolean.toString(unique))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(Collections.singletonList(statsDto))));
        verify(hitService, times(1)).findStats(
                any(LocalDateTime.class), any(LocalDateTime.class), any(String[].class), anyBoolean());
    }

    @Test
    void stats_whenInvalidDateFormat_thenResponseStatusBadRequest() throws Exception {
        final String start = DateTimeFormatter.ofPattern(DATE_FORMAT).format(LocalDateTime.now());
        final List<String> uris = List.of("uri");
        final boolean unique = false;

        when(hitService.findStats(any(LocalDateTime.class), any(LocalDateTime.class),
                any(String[].class), anyBoolean()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        mvc.perform(get(STATS_PATH)
                        .param("start", start)
                        .param("end", "bad_date")
                        .param("uris", uris.toArray(new String[0]))
                        .param("unique", Boolean.toString(unique))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(hitService, times(0)).findStats(
                any(LocalDateTime.class), any(LocalDateTime.class), any(String[].class), anyBoolean());
    }
}