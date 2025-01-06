package ru.practicum.ewm.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.CompilationDto;
import ru.practicum.ewm.base.dto.NewCompilationDto;
import ru.practicum.ewm.base.dto.UpdateCompilationRequest;
import ru.practicum.ewm.base.exceptions.DataNotFoundException;
import ru.practicum.ewm.base.mapper.CompilationMapper;
import ru.practicum.ewm.base.model.Compilation;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.repository.CompilationRepository;
import ru.practicum.ewm.base.repository.EventRepository;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCompilationService  {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private Compilation findById(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Compilation not found" +  compId));
    }

    private Set<Event> findEvents(Set<Long> events) {
        if (events == null) {
            return Set.of();
        } else {
            return eventRepository.findAllByIdIn(events);
        }
    }

    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.mapToEntity(newCompilationDto, findEvents(newCompilationDto.getEvents()));
        compilation = compilationRepository.save(compilation);
        log.info("Save compilation {}", compilation.getTitle());
        return CompilationMapper.mapToDto(compilation);
    }

    public CompilationDto update(UpdateCompilationRequest request, Long compId) {
        Compilation updatedCompilation = CompilationMapper.updateFields(findById(compId), request, findEvents(request.getEvents()));
        updatedCompilation = compilationRepository.save(updatedCompilation);
        log.info("Update compilation {}", updatedCompilation.getTitle());
        return CompilationMapper.mapToDto(updatedCompilation);
    }

    public void delete(Long compId) {
        Compilation compilation = findById(compId);
        compilationRepository.deleteById(compId);
        log.info("Delete compilation {}", compilation.getTitle());
    }
}
