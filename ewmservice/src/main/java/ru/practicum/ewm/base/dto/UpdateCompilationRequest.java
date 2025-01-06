package ru.practicum.ewm.base.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;

    @Length(min = 1, max = 50)
    private String title;

    public boolean hasEvents() {
        return events != null;
    }

    public boolean hasPinned() {
        return pinned != null;
    }

    public boolean hasTitle() {
        return title != null;
    }
}
