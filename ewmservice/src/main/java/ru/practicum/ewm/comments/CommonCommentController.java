package ru.practicum.ewm.comments;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommonCommentController {
    private final CommentService commentService;

    @GetMapping("/event/{eventId}")
    List<CommentDto> getComments(@PathVariable Long eventId,
                                 @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                 @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        log.info("GET /comments/event/{eventId} eventId={}", eventId);
        return commentService.getByEvent(eventId, from, size);
    }
}
