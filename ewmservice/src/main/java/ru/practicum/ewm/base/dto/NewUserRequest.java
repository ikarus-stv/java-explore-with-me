package ru.practicum.ewm.base.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "email expected")
    @Email(message = "Invalid email")
    @Length(min = 6, max = 254)
    private String email;

    @NotBlank(message = "username expected")
    @Length(min = 2, max = 250)
    private String name;
}
