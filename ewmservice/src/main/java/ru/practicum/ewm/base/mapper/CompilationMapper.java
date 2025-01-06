package ru.practicum.ewm.base.mapper;

import ru.practicum.ewm.base.dto.CompilationDto;
import ru.practicum.ewm.base.dto.NewCompilationDto;
import ru.practicum.ewm.base.dto.UpdateCompilationRequest;
import ru.practicum.ewm.base.model.Compilation;
import ru.practicum.ewm.base.model.Event;

import java.util.List;
import java.util.Set;

public class CompilationMapper {

    public static Compilation mapToEntity(NewCompilationDto request, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned((request.getPinned() == null) ? Boolean.FALSE : request.getPinned());
        compilation.setTitle(request.getTitle());
        return compilation;
    }

    public static CompilationDto mapToDto(Compilation compilation) {
        CompilationDto dto = new CompilationDto();
        dto.setId(compilation.getId());
        dto.setEvents(EventMapper.mapToListShortDto(compilation.getEvents()));
        dto.setPinned(compilation.getPinned());
        dto.setTitle(compilation.getTitle());
        return dto;
    }

    public static Compilation updateFields(Compilation compilation, UpdateCompilationRequest updateCompilationRequest,
                                           Set<Event> events) {
        if (updateCompilationRequest.hasEvents()) {
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.hasPinned()) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.hasTitle()) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        return compilation;
    }

    public static List<CompilationDto> mapToListDto(List<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::mapToDto).toList();
    }
}
