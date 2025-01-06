package ru.practicum.ewm.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.base.dto.LocationDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(message = "annotation expected")
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    @Positive(message = "ID must be positive")
    private Long category;

    @NotBlank(message = "description expected")
    @Length(min = 20, max = 7000)
    private String description;

    @NotNull(message = "eventDate expected")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @Valid
    @NotNull(message = "Location expected")
    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Long participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "title expected")
    @Length(min = 3, max = 120)
    private String title;

    public boolean hasPaid() {
        return paid != null;
    }

    public boolean hasParticipantLimit() {
        return participantLimit != null;
    }

    public boolean hasRequestModeration() {
        return requestModeration != null;
    }
}
