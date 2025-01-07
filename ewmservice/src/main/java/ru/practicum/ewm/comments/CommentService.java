package ru.practicum.ewm.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.base.enums.EventStates;
import ru.practicum.ewm.base.exceptions.BadRequestException;
import ru.practicum.ewm.base.exceptions.DataNotFoundException;
import ru.practicum.ewm.base.model.Event;
import ru.practicum.ewm.base.model.User;
import ru.practicum.ewm.base.repository.EventRepository;
import ru.practicum.ewm.base.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentMapper mapper;

    public CommentDto save(Long userId, Long eventId, NewCommentDto newCommentDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Event not found id=" +  eventId)));

        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found id=" + userId));

        if (!event.getState().equals(EventStates.PUBLISHED)) {
            throw new BadRequestException("Comments can be created only for published events.");
        }

        Comment comment = mapper.mapToEntity(newCommentDto);
        comment.setAuthor(userId);
        comment.setEvent(eventId);
        comment.setCreateDateTime(LocalDateTime.now());

        comment = commentRepository.save(comment);
        return mapper.mapToDto(comment);
    }

    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public CommentDto update(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Event not found id=" +  eventId)));

        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found id=" + userId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment not found id=" + commentId));

        if (!comment.getEvent().equals(eventId)) {
            throw new BadRequestException("Comments is for other event");
        }

        CommentMapper.updateFields(comment, newCommentDto);
        commentRepository.save(comment);
        return mapper.mapToDto(comment);
    }

    public List<CommentDto> getByAuthor(Long userId, Integer from, Integer size) {
        userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found id=" + userId));
        List<Comment> comments = commentRepository.findAllByAuthor(userId, PageRequest.of(from / size, size));
        return mapper.mapToListDto(comments);
    }

    public void delete(Long userId, Long commentId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found id=" + userId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment not found id=" + commentId));

        if (!comment.getAuthor().equals(author.getId())) {
            throw new BadRequestException("Only author can delete comment");
        }
        commentRepository.deleteById(commentId);
    }

    public List<CommentDto> getByEvent(Long eventId, Integer from, Integer size) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException(String.format("Event not found id=" +  eventId)));
        return commentRepository.findAllByEvent(eventId, PageRequest.of(from / size, size))
                .stream()
                .map(mapper::mapToDto)
                .collect(Collectors.toList());
    }
}
