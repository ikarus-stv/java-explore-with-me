package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"app", "uri", "hits"})

public class ViewStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
