package ru.practicum.ewm.base.model;

import jakarta.persistence.Embeddable;
import lombok.experimental.FieldDefaults;
import lombok.*;

@Embeddable
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"lat", "lon"})
public class Location {
    private Float lat;
    private Float lon;
}
