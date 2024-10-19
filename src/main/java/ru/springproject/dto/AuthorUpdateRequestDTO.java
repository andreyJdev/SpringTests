package ru.springproject.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public record AuthorUpdateRequestDTO (

        @Size(min = 2, max = 32, message = "{errors.author.name_size_invalid}")
        @NotNull(message = "{errors.author.name_is_null}")
        String name,

        @NotNull(message = "{errors.author.date_is_null}")
        @Past(message = "{errors.author.date_is_invalid}")
        LocalDate dateOfBirth,

        Set<Long> books
) {
}
