package ru.practicum.ewm.comments;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class PersonalCommentController {
    private final CommentService commentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto save(@PathVariable Long userId,
                           @PathVariable Long eventId,
                           @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("POST /users/{userId}/comments/{eventId} {}", newCommentDto);
        return commentService.save(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{eventId}/{commentId}")
    public CommentDto update(@PathVariable Long userId,
                             @PathVariable Long eventId,
                             @PathVariable Long commentId,
                             @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("PATCH /users/{userId}/comments/{eventId}/{commentId} {}", newCommentDto);
        return commentService.update(userId, eventId, commentId, newCommentDto);
    }

    @GetMapping
    public List<CommentDto> getByAuthor(@PathVariable Long userId,
                                        @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("GET /users/{userId}/comments userId = {}", userId);
        return commentService.getByAuthor(userId, from, size);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId,
                       @PathVariable Long commentId) {
        log.info("DELETE /users/{userId}/comments/{commentId} userId={} commentId={}", userId, commentId);
        commentService.delete(userId, commentId);
    }

}
