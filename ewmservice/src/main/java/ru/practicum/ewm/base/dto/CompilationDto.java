package ru.practicum.ewm.base.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.base.dto.EventShortDto;

import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Long id;

    Set<EventShortDto> events;

    Boolean pinned;

    String title;
}
