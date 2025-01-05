package ru.practicum.ewm.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.admin.dto.NewParamEventDto;
import ru.practicum.ewm.base.dto.EventFullDto;
import ru.practicum.ewm.base.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.base.enums.States;
import ru.practicum.ewm.base.exceptions.BadRequestException;
import ru.practicum.ewm.base.exceptions.ConflictException;
import ru.practicum.ewm.base.exceptions.NotFoundException;
import ru.practicum.ewm.base.mapper.EventMapper;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.repository.CategoryRepository;
import ru.practicum.ewm.base.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminEventService  {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    private Event findById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found: " + eventId));
    }

    private Category findCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category not found: " + catId));
    }

    public Collection<EventFullDto> getEvents(NewParamEventDto newParamEventDto) {
        PageRequest pageRequest = PageRequest.of(newParamEventDto.getFrom() / newParamEventDto.getSize(),
                newParamEventDto.getSize(),
                Sort.by(Sort.Direction.ASC, "id"));

        List<Event> events = eventRepository.findAllWithCriteria(pageRequest, newParamEventDto).toList();

        log.info("Get events: {} ", events.size());
        return EventMapper.mapToListDto(events);
    }

    public EventFullDto update(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {

        if (updateEventAdminRequest.hasEventDate() && updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new BadRequestException("Invalid eventDate: " + updateEventAdminRequest.getEventDate());
        }

        Event findEvent = findById(eventId);

        if (findEvent.getState().equals(States.PUBLISHED)) {
            throw new ConflictException("Already PUBLISHED");
        } else if (findEvent.getState().equals(States.CANCELED)) {
            throw new ConflictException("Already CANCELED");
        }

        Category category = null;
        if (updateEventAdminRequest.hasCategory() && !findEvent.getCategory().getId().equals(updateEventAdminRequest.getCategory())) {
            category = findCategoryById(updateEventAdminRequest.getCategory());
        }

        Event updatedEvent = EventMapper.updateAdminFields(findEvent, updateEventAdminRequest, category);

        try {
            updatedEvent = eventRepository.save(updatedEvent);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getMessage(), e);
        }

        log.info("Event updated by admin : {}", updatedEvent.getTitle());
        return EventMapper.mapToDto(updatedEvent);
    }
}
