package ru.practicum.server.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"app", "uri", "hits"})
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
