package ru.practicum.ewm.base.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    @NotNull(message = "ID required")
    private List<Long> requestIds;
    @NotBlank(message = "Status expected")
    private String status;
}
