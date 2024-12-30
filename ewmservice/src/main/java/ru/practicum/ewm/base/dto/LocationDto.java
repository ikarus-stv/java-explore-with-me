package ru.practicum.ewm.base.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"lat", "lon"})
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotNull(message = "Широта должна быть указана")
    private Float lat;

    @NotNull(message = "Долгота должна быть указана")
    private Float lon;
}
