package ru.practicum.ewm.common.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestParam {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private String sort;
    private Integer from;
    private Integer size;
    private HttpServletRequest request;

    public boolean expectedBaseCriteria() {
        return (this.text != null && this.text.equals("0"))
                && ((this.categories.size() == 1) && (this.categories.getFirst().equals(0L)));
    }
}
