package ru.springproject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import ru.springproject.utils.Genres;

import java.time.LocalDate;
import java.util.Set;

public record BookUpdateRequestDTO(

        @NotEmpty(message = "{errors.book.title_is_empty}")
        @Size(min = 2, max = 32, message = "{errors.book.title_size_invalid}")
        String title,

        Genres genre,

        @NotNull(message = "{errors.book.date_is_null}")
        @Past(message = "{errors.book.date_is_invalid}")
        LocalDate publicationDate,

        Set<Long> authors) {
}
