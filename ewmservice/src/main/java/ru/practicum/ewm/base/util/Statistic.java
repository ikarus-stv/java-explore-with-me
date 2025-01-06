package ru.practicum.ewm.base.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.ewm.base.model.Event;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class Statistic {

    StatsClient statsClient;

    String serviceName;

    @Autowired
    public Statistic(StatsClient statsClient, @Value("${ewm.service.name}") String serviceName) {
        this.statsClient = statsClient;
        this.serviceName = serviceName;
    }

    private List<Long> getEventList(ViewStatsDto statsDto) {
        if (statsDto.getUri() != null && !statsDto.getUri().isEmpty() && !statsDto.getUri().equals(" ")) {
            return Arrays.stream(statsDto.getUri().split("/"))
                    .skip(1L)
                    .filter(elem -> elem.chars().allMatch(Character::isDigit))
                    .map(Long::valueOf)
                    .toList();
        }

        return List.of();
    }

    private LocalDateTime findMinDate(Set<Event> events) {
        LocalDateTime defValue = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
        LocalDateTime result = events.stream()
                .map(Event::getPublishedOn)
                .min(LocalDateTime::compareTo)
                .orElse(defValue);
        return result;
    }

    private LocalDateTime findMaxDate(Set<Event> events) {
        LocalDateTime defValue = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
        LocalDateTime result = events.stream()
                .map(Event::getEventDate)
                .max(LocalDateTime::compareTo)
                .orElse(defValue);
        return result;
    }

    private Map<Long, Long> findStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        log.info("*** Call  statsClient.get(start, end, uris, unique)");
        log.info("*** Call  statsClient.get start = {} ", start);
        log.info("*** Call  statsClient.get end = {} ", end);
        log.info("*** Call  statsClient.get uris = {} ", uris);
        log.info("*** Call  statsClient.get unique = {}", unique);
        List<ViewStatsDto> viewStatsDtoList = statsClient.get(start, end, uris, unique);

        log.info("statsClient.get returns {}", viewStatsDtoList);

        return viewStatsDtoList
                .stream()
                .collect(Collectors.toMap(elem -> getEventList(elem).getFirst(), ViewStatsDto::getHits));
    }

    public Map<Long, Long> getStatistic(Set<Event> events, Boolean unique) {
        LocalDateTime rangeStart = findMinDate(events);
        LocalDateTime rangeEnd = findMaxDate(events);
        List<String> uris = events.stream().map(elem -> "/events/" + elem.getId()).toList();
        return findStatistic(rangeStart, rangeEnd, uris, unique);
    }

    public Map<Long, Long> getStatistic(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return findStatistic(start, end, uris, unique);
    }

    public void saveStatistic(HttpServletRequest request) {
        EndpointHitDto dto = new EndpointHitDto();
        dto.setApp(serviceName);
        dto.setUri(request.getRequestURI());
        dto.setIp(request.getRemoteAddr());
        dto.setTimestamp(LocalDateTime.now());
        log.info("*** Call statsClient.save(dto) {}", dto);

        statsClient.save(dto);
    }
}
