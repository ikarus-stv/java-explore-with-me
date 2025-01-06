package ru.practicum.ewm.base.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.admin.dto.NewParamEventDto;
import ru.practicum.ewm.base.model.Category;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.EventCriteria;
import ru.practicum.ewm.base.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventCriteriaRepositoryImpl implements EventCriteriaRepository {
    private final EntityManager manager;

    private final CriteriaBuilder builder;

    public EventCriteriaRepositoryImpl(EntityManager entityManager) {
        this.manager = entityManager;
        this.builder = entityManager.getCriteriaBuilder();
    }

    private Predicate getPredicates(EventCriteria criteria, Root<Event> eventRoot) {
        List<Predicate> predicates = new ArrayList<>();
        Predicate annotationPredicate = null;

        if (criteria.getText() != null) {
            annotationPredicate = builder.like(builder.lower(eventRoot.get("annotation")),
                    "%" + criteria.getText().toLowerCase() + "%");
        }

        if (criteria.getText() != null) {
            if (annotationPredicate == null) {
                Predicate description = builder.like(builder.lower(eventRoot.get("description")),
                        "%" + criteria.getText().toLowerCase() + "%");
                predicates.add(description);
            } else {
                Predicate descriptionPredicate = builder.like(builder.lower(eventRoot.get("description")),
                        "%" + criteria.getText().toLowerCase() + "%");
                predicates.add(builder.or(annotationPredicate, descriptionPredicate));
            }
        }

        if (criteria.getCategories() != null && !criteria.getCategories().isEmpty()) {
            Join<Event, Category> categoryJoin = eventRoot.join("category");
            Predicate predicate = categoryJoin.get("id").in(criteria.getCategories());
            predicates.add(predicate);
        }

        if (criteria.getPaid() != null && !criteria.getPaid().equals(Boolean.TRUE)) {
            Predicate predicate = builder.equal(eventRoot.get("paid"), criteria.getPaid());
            predicates.add(predicate);
        }

        if (criteria.getRangeStart() != null || criteria.getRangeEnd() != null) {
            LocalDateTime rangeStart = criteria.getRangeStart() != null ? criteria.getRangeStart() : LocalDateTime.MIN;

            LocalDateTime rangeEnd = criteria.getRangeEnd() != null ? criteria.getRangeEnd() : LocalDateTime.MAX;
            Predicate eventDate = builder.between(eventRoot.get("eventDate"), rangeStart, rangeEnd);
            predicates.add(eventDate);
        } else {
            Predicate eventDate = builder.between(eventRoot.get("eventDate"),
                    LocalDateTime.of(1900, 1, 1, 0, 0, 0),
                    LocalDateTime.of(9999, 12, 31, 23, 59, 59));
            predicates.add(eventDate);
        }

        return builder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
    }

    @Override
    public Page<Event> findAllWithCriteria(PageRequest pageRequest, EventCriteria criteria) {
        CriteriaQuery<Event> criteriaQuery = builder.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        Predicate predicate = getPredicates(criteria, eventRoot);
        criteriaQuery.where(predicate);

        if (pageRequest.getSort().isUnsorted()) {
            criteriaQuery.orderBy(builder.desc(eventRoot.get("createdOn")));
        }

        TypedQuery<Event> query = manager.createQuery(criteriaQuery);
        query.setFirstResult(pageRequest.getPageSize() * pageRequest.getPageNumber());
        query.setMaxResults(pageRequest.getPageSize());

        List<Event> events = query.getResultList();

        return new PageImpl<>(events);
    }

    private Predicate getPredicates(NewParamEventDto params, Root<Event> eventRoot) {
        List<Predicate> predicates = new ArrayList<>();

        if (params.getUsers() != null && !params.getUsers().isEmpty()) {
            Join<Event, User> initiatorJoin = eventRoot.join("initiator");
            Predicate id = initiatorJoin.get("id").in(params.getUsers());
            predicates.add(id);
        }

        if (params.getStates() != null && !params.getStates().isEmpty()) {
            Predicate predicate = eventRoot.get("state").in(params.getStates());
            predicates.add(predicate);
        }

        if (params.getCategories() != null && !params.getCategories().isEmpty()) {
            Join<Event, Category> categoryJoin = eventRoot.join("category");
            Predicate id = categoryJoin.get("id").in(params.getCategories());
            predicates.add(id);
        }

        if (params.getRangeStart() != null || params.getRangeEnd() != null) {
            LocalDateTime rangeStart = params.getRangeStart() != null ? params.getRangeStart() : LocalDateTime.MIN;

            LocalDateTime rangeEnd = params.getRangeEnd() != null ? params.getRangeEnd() : LocalDateTime.MAX;
            Predicate eventDate = builder.between(eventRoot.get("eventDate"), rangeStart, rangeEnd);
            predicates.add(eventDate);
        } else {
            Predicate eventDate = builder.between(eventRoot.get("eventDate"),
                    LocalDateTime.of(1900, 1, 1, 0, 0, 0),
                    LocalDateTime.of(9999, 12, 31, 23, 59, 59));
            predicates.add(eventDate);
        }

        return builder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
    }

    @Override
    public Page<Event> findAllWithCriteria(PageRequest pageRequest, NewParamEventDto params) {
        CriteriaQuery<Event> criteriaQuery = builder.createQuery(Event.class);
        Root<Event> eventRoot = criteriaQuery.from(Event.class);
        Predicate predicate = getPredicates(params, eventRoot);
        criteriaQuery.where(predicate);

        if (pageRequest.getSort().isUnsorted()) {
            criteriaQuery.orderBy(builder.desc(eventRoot.get("createdOn")));
        }

        TypedQuery<Event> query = manager.createQuery(criteriaQuery);
        query.setFirstResult(pageRequest.getPageSize() * pageRequest.getPageNumber());
        query.setMaxResults(pageRequest.getPageSize());

        List<Event> events = query.getResultList();

        return new PageImpl<>(events);
    }
}
