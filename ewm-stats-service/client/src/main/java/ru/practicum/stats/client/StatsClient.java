package ru.practicum.stats.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.client.baseclient.BaseClient;
import ru.practicum.stats.dto.HitDto;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class StatsClient extends BaseClient {
    private static final String HIT_PATH = "/hit";
    private static final String STATS_PATH = "/stats";
    private static final String STATS_PARAM_START_NAME = "start";
    private static final String STATS_PARAM_END_NAME = "end";
    private static final String STATS_PARAM_URIS_NAME = "uris";
    private static final String STATS_PARAM_UNIQUE_NAME = "unique";

    public StatsClient(@Value("${ewm-stats-service.uri}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        Map<String, Object> param = Map.of(
                STATS_PARAM_START_NAME, start,
                STATS_PARAM_END_NAME, end,
                STATS_PARAM_URIS_NAME, uris,
                STATS_PARAM_UNIQUE_NAME, unique
        );
        return get(STATS_PATH + "?" + STATS_PARAM_START_NAME + "={" + STATS_PARAM_START_NAME + "}&" +
                STATS_PARAM_END_NAME + "={" + STATS_PARAM_END_NAME + "}&" +
                STATS_PARAM_URIS_NAME + "={" + STATS_PARAM_URIS_NAME + "}&" +
                STATS_PARAM_UNIQUE_NAME + "={" + STATS_PARAM_UNIQUE_NAME + "}", param);
    }

    public ResponseEntity<Object> createHit(HitDto hitDto) {
        return post(HIT_PATH, hitDto);
    }
}
