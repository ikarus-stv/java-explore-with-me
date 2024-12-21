package ru.practicum.server.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("""
	    SELECT new ru.practicum.server.model.ViewStats(eh.app, eh.uri, COUNT(DISTINCT eh.ip))
            FROM EndpointHit AS eh
            WHERE eh.timestamp BETWEEN :start and :end
            AND (eh.uri IN (:uris) OR (:uris) is NULL)
            GROUP BY eh.app, eh.uri
            ORDER BY COUNT(DISTINCT eh.ip) DESC
		""")
    List<ViewStats> getUniqueHits(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                                  @Param("uris") List<String> uris, PageRequest pageable);

    @Query("""
            SELECT new ru.practicum.server.model.ViewStats(eh.app, eh.uri, COUNT(eh.ip))
            FROM EndpointHit AS eh
            WHERE eh.timestamp BETWEEN :start and :end
            AND (eh.uri IN (:uris) OR (:uris) is NULL)
            GROUP BY eh.app, eh.uri
            ORDER BY COUNT(eh.ip) DESC
		""")
    List<ViewStats> getHits(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end,
                            @Param("uris") List<String> uris, PageRequest pageable);
}
