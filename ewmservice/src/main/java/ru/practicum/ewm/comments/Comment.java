package ru.practicum.ewm.comments;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(name = "author_id", nullable = false)
    private Long author;

    @Column(name = "event_id", nullable = false)
    private Long event;

    @Column(name = "create_datetime", nullable = false)
    private LocalDateTime createDateTime;
}
