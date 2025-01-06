package ru.practicum.ewm.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.base.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByRequesterId(Long requesterId);

    boolean existsByEventIdAndRequesterId(Long eventId, Long requesterId);

    Optional<Request> findByIdAndRequesterId(Long requestId, Long requesterId);
}