package ru.practicum.ewm.base.dto;

import lombok.*;
import ru.practicum.ewm.base.enums.AdminEventAction;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private AdminEventAction stateAction;

    public boolean hasStateAction() {
        return stateAction != null;
    }
}