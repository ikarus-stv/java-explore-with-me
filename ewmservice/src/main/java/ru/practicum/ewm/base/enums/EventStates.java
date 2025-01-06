package ru.practicum.ewm.base.enums;

import java.util.Optional;

public enum EventStates {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static Optional<EventStates> ofString(String stringState) {
        for (EventStates state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
