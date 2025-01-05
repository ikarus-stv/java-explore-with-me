package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.server.mapper.EndpointHitMapper;
import ru.practicum.server.mapper.ViewStatsMapper;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatService {
    private final StatsRepository repository;
    private final EndpointHitMapper endpointHitMapper;
    private final ViewStatsMapper viewStatsMapper;

    public List<ViewStatsDto> get(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (unique) {
            log.info("Get stat {} - {}", start, end);
            return viewStatsMapper.mapToListDto(repository.getUniqueHits(start.minusSeconds(1), end.plusSeconds(1), uris));
        } else {
            log.info("Get stat (unique) {} - {}", start, end);
            return viewStatsMapper.mapToListDto(repository.getHits(start.minusSeconds(1), end.plusSeconds(1), uris));
        }
    }

    public EndpointHitDto save(EndpointHitDto dto) {
        log.info("Save stat {}/{}/{}", dto.getApp(), dto.getUri(), dto.getId());
        return endpointHitMapper.mapToDto(repository.save(endpointHitMapper.mapToEntity(dto)));
    }
}
