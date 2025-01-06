package ru.practicum.ewm.base.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"lat", "lon"})
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotNull(message = "latitude expected")
    private Float lat;
    @NotNull(message = "longitude expected")
    private Float lon;
}
