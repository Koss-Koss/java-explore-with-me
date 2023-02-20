package ru.practicum.stats.server.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.stats.server.model.Hit;
import ru.practicum.stats.server.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class HitRepositoryTest {

    @Autowired
    private HitRepository hitRepository;

    private String app1;
    private String app2;
    private String uri1;
    private String uri2;
    private String uri3;
    private String ip1;
    private String ip2;
    private String ip3;
    private LocalDateTime dt;
    private Hit hit1;
    private Hit hit2;
    private Hit hit3;
    private Hit hit4;
    private Hit hit5;

    @BeforeEach
    void setUp() {
        app1 = "app1";
        app2 = "app2";
        uri1 = "uri1";
        uri2 = "uri2";
        uri3 = "uri3";
        ip1 = "192.168.0.1";
        ip2 = "138.7.14.17";
        ip3 = "150.1.2.3";
        dt = LocalDateTime.now();

        hit1 = hitRepository.save(Hit.builder().app(app1).uri(uri1).ip(ip1).timestamp(dt.plusMinutes(5)).build());
        hit2 = hitRepository.save(Hit.builder().app(app1).uri(uri1).ip(ip1).timestamp(dt.plusMinutes(10)).build());
        hit3 = hitRepository.save(Hit.builder().app(app1).uri(uri1).ip(ip2).timestamp(dt.plusMinutes(15)).build());
        hit4 = hitRepository.save(Hit.builder().app(app1).uri(uri2).ip(ip2).timestamp(dt.plusMinutes(20)).build());
        hit5 = hitRepository.save(Hit.builder().app(app2).uri(uri3).ip(ip3).timestamp(dt.plusMinutes(25)).build());
    }

    @Test
    void findAllNotUniqueIP() {
        //Выборка по uri
        List<Stats> result =
                hitRepository.findAllNotUniqueIP(dt.plusMinutes(2), dt.plusMinutes(30), List.of(uri1, uri3));

        assertEquals(2, result.size());
        assertEquals(hit1.getUri(), result.get(0).getUri());
        assertEquals(3, result.get(0).getHits());
        assertEquals(hit5.getUri(), result.get(1).getUri());
        assertEquals(1, result.get(1).getHits());

        //Выборка по uri и времени
        result = hitRepository.findAllNotUniqueIP(dt.plusMinutes(7), dt.plusMinutes(22), List.of(uri1, uri2));

        assertEquals(2, result.size());
        assertEquals(hit1.getUri(), result.get(0).getUri());
        assertEquals(2, result.get(0).getHits());
        assertEquals(hit4.getUri(), result.get(1).getUri());
        assertEquals(1, result.get(1).getHits());

        //Выборка единственного по uri и времени
        result = hitRepository.findAllNotUniqueIP(dt.plusMinutes(17), dt.plusMinutes(22), List.of(uri1, uri2));

        assertEquals(1, result.size());
        assertEquals(hit4.getUri(), result.get(0).getUri());
        assertEquals(1, result.get(0).getHits());

        //Пустая выборка по uri и времени
        result = hitRepository.findAllNotUniqueIP(dt.plusMinutes(2), dt.plusMinutes(30), List.of("not uri"));

        assertEquals(0, result.size());

        //Пустая выборка по времени
        result = hitRepository.findAllNotUniqueIP(dt.plusMinutes(30), dt.plusMinutes(100), List.of(uri2, uri3));

        assertEquals(0, result.size());
    }

    @Test
    void findAllUniqueIp() {
        //Выборка по uri
        List<Stats> result =
                hitRepository.findAllUniqueIP(dt.plusMinutes(2), dt.plusMinutes(30), List.of(uri1, uri3));

        assertEquals(2, result.size());
        assertEquals(hit1.getUri(), result.get(0).getUri());
        assertEquals(2, result.get(0).getHits());
        assertEquals(hit5.getUri(), result.get(1).getUri());
        assertEquals(1, result.get(1).getHits());

        //Выборка по uri и времени
        result = hitRepository.findAllUniqueIP(dt.plusMinutes(2), dt.plusMinutes(22), List.of(uri1, uri2));

        assertEquals(2, result.size());
        assertEquals(hit1.getUri(), result.get(0).getUri());
        assertEquals(2, result.get(0).getHits());
        assertEquals(hit4.getUri(), result.get(1).getUri());
        assertEquals(1, result.get(1).getHits());

        //Выборка единственного по uri и времени
        result = hitRepository.findAllUniqueIP(dt.plusMinutes(22), dt.plusMinutes(30), List.of(uri2, uri3));

        assertEquals(1, result.size());
        assertEquals(hit5.getUri(), result.get(0).getUri());
        assertEquals(1, result.get(0).getHits());

        //Пустая выборка по uri и времени
        result = hitRepository.findAllUniqueIP(dt.plusMinutes(2), dt.plusMinutes(30), List.of("not uri"));

        assertEquals(0, result.size());

        //Пустая выборка по времени
        result = hitRepository.findAllUniqueIP(dt.plusMinutes(30), dt.plusMinutes(100), List.of(uri2, uri3));

        assertEquals(0, result.size());
    }
}