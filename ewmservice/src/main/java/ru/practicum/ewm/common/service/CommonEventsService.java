package ru.practicum.ewm.common.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.dto.EventFullDto;
import ru.practicum.ewm.base.dto.EventShortDto;
import ru.practicum.ewm.base.enums.EventStates;
import ru.practicum.ewm.base.exceptions.BadRequestException;
import ru.practicum.ewm.base.exceptions.DataNotFoundException;
import ru.practicum.ewm.base.mapper.EventMapper;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.EventCriteria;
import ru.practicum.ewm.base.repository.EventRepository;
import ru.practicum.ewm.base.util.Statistic;
import ru.practicum.ewm.common.controller.EventRequestParam;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonEventsService {

    private final EventRepository eventRepository;

    private final Statistic statistic;

    private PageRequest getPageable(String sort, Integer from, Integer size) {
        String sortBy;
        if (sort == null || sort.equalsIgnoreCase("EVENT_DATE")) {
            sortBy = "event_date";
        } else if (sort.equalsIgnoreCase("VIEWS")) {
            sortBy = "views";
        } else {
            return null;
        }
        return PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, sortBy));
    }

    private Event findById(Long id) {
        Optional<Event> event = eventRepository.findById(id);

        if (event.isEmpty() || !event.get().getState().equals(EventStates.PUBLISHED)) {
            throw new DataNotFoundException("Event not available, ID =" + id);
        }

        return event.get();
    }

    public Collection<EventShortDto> getAll(EventRequestParam params) {
        if (params.expectedBaseCriteria()) {
            throw new BadRequestException(String.format("Не заданы базовые параметры поиска. text = %s " +
                    "и единственное значение category = %s", params.getText(), params.getCategories().getFirst()));
        }

        PageRequest pageRequest = getPageable(params.getSort(), params.getFrom(), params.getSize());
        EventCriteria eventCriteria = new EventCriteria(params.getText(),
                params.getCategories(),
                params.getPaid(),
                params.getRangeStart(),
                params.getRangeEnd(),
                params.getOnlyAvailable());

        Set<Event> events = eventRepository.findAllWithCriteria(pageRequest, eventCriteria).toSet();

        Map<Long, Long> statsMap = statistic.getStatistic(events, Boolean.FALSE);

        statistic.saveStatistic(params.getRequest());
        log.info("Получаем публичный список из {} событий с добавлением в статистику", events.size());
        return EventMapper.mapToListShortDto(events, statsMap);
    }

    public EventFullDto get(Long id, HttpServletRequest request) {
        Event event = findById(id);

        Map<Long, Long> statsMap = statistic.getStatistic(
                event.getPublishedOn(),
                LocalDateTime.now(),
                List.of(request.getRequestURI()),
                Boolean.TRUE
        );

        statistic.saveStatistic(request);
        log.info("Получаем полную информацию о событии {} с добавлением в статистику", event.getTitle());
        return EventMapper.mapToDtoWithStat(event, statsMap.getOrDefault(id, 0L));
    }
}
