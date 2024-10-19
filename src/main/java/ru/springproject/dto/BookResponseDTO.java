package ru.springproject.dto;

import com.fasterxml.jackson.annotation.JsonView;
import ru.springproject.models.Book;
import ru.springproject.utils.Views;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record BookResponseDTO(

        @JsonView(Views.Public.class)
        Long id,

        @JsonView(Views.Public.class)
        String title,

        @JsonView(Views.Public.class)
        String genre,

        @JsonView(Views.Public.class)
        LocalDate publicationDate,

        @JsonView(Views.Public.class)
        Set<AuthorInBookDTO> authors) {


    public BookResponseDTO(Book book) {
        this(
                book.getId(),
                book.getTitle(),
                capitalizeFirstLetter(book.getGenre().name().toLowerCase()),
                book.getPublicationDate(),
                book.getAuthors()
                        .stream()
                        .map(AuthorInBookDTO::new)
                        .collect(Collectors.toSet())
        );
    }

    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
