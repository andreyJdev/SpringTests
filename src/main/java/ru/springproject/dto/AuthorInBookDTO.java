package ru.springproject.dto;

import ru.springproject.models.Author;

import java.time.LocalDate;

public record AuthorInBookDTO(String name, LocalDate dateOfBirth) {

        public AuthorInBookDTO(Author author) {
                this(author.getName(), author.getDateOfBirth());
        }
}
