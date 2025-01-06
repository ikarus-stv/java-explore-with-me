package ru.practicum.ewm.base.dto;

import lombok.*;
import ru.practicum.ewm.base.enums.UserEventAction;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest extends UpdateEventRequest {
    private UserEventAction stateAction;

    public boolean hasStateAction() {
        return stateAction != null;
    }
}
