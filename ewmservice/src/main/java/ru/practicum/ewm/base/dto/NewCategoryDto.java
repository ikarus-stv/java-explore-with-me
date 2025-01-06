package ru.practicum.ewm.base.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"name"})
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(message = "Название добавляемой категории не может быть пустым!")
    @Length(min = 1, max = 50)
    private String name;
}
