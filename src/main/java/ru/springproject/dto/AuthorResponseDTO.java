package ru.springproject.dto;

import com.fasterxml.jackson.annotation.JsonView;
import ru.springproject.models.Author;
import ru.springproject.utils.Views;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record AuthorResponseDTO(

        @JsonView(Views.Internal.class)
        Long id,

        @JsonView(Views.Public.class)
        String name,

        @JsonView(Views.Public.class)
        LocalDate dateOfBirth,

        @JsonView(Views.Internal.class)
        Set<BookInAuthorDTO> books) {

    public AuthorResponseDTO(Author author) {
        this(
                author.getId(),
                author.getName(),
                author.getDateOfBirth(),
                author.getBooks()
                        .stream()
                        .map(BookInAuthorDTO::new)
                        .collect(Collectors.toSet())
        );
    }
}
