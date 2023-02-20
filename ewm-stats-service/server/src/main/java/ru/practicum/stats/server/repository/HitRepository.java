package ru.practicum.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.server.model.Hit;
import ru.practicum.stats.server.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {

    @Query(value = "SELECT new ru.practicum.stats.server.model.Stats(h.app, h.uri, COUNT (h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN ?3 " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.ip) DESC")
    List<Stats> findAllNotUniqueIP(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "SELECT new ru.practicum.stats.server.model.Stats(h.app, h.uri, COUNT (DISTINCT h.ip)) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 AND h.uri IN ?3 " +
            "GROUP BY h.app, h.uri ORDER BY COUNT(h.ip) DESC")
    List<Stats> findAllUniqueIP(LocalDateTime start, LocalDateTime end, List<String> uris);
}
