package ru.practicum.ewm.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.CompilationDto;
import ru.practicum.ewm.base.exceptions.DataNotFoundException;
import ru.practicum.ewm.base.mapper.CompilationMapper;
import ru.practicum.ewm.base.model.Compilation;
import ru.practicum.ewm.base.repository.CompilationRepository;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonCompilationsService {

    private final CompilationRepository compilationRepository;

    public Compilation findById(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Compilation not found, id = " + compId));
    }

    public Collection<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));

        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageRequest);
        } else {
            compilations = compilationRepository.findAll(pageRequest).toList();
        }

        return CompilationMapper.mapToListDto(compilations);
    }

    public CompilationDto get(Long compId) {
        return CompilationMapper.mapToDto(findById(compId));
    }
}
