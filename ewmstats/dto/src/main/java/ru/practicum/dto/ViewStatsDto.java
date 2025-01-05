package ru.practicum.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"app", "uri", "hits"})
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private Long hits;
}
