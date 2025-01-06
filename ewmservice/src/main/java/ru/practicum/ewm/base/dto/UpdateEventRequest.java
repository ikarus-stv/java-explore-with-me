package ru.practicum.ewm.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.base.dto.LocationDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    @Length(min = 20, max = 2000)
    private String annotation;

    @Positive(message = "ID must be positive")
    private Long category;

    @Length(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @Valid
    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(message = "participantLimit can't be negative")
    private Long participantLimit;

    private Boolean requestModeration;

    @Length(min = 3, max = 120)
    private String title;

    public boolean hasAnnotation() {
        return annotation != null;
    }

    public boolean hasCategory() {
        return category != null;
    }

    public boolean hasDescription() {
        return description != null;
    }

    public boolean hasEventDate() {
        return eventDate != null;
    }

    public boolean hasLocation() {
        return location != null;
    }

    public boolean hasPaid() {
        return paid != null;
    }

    public boolean hasParticipantLimit() {
        return participantLimit != null;
    }

    public boolean hasRequestModeration() {
        return requestModeration != null;
    }

    public boolean hasTitle() {
        return title != null;
    }
}