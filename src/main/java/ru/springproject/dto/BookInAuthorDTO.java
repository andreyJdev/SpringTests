package ru.springproject.dto;

import ru.springproject.models.Book;

import java.time.LocalDate;

public record BookInAuthorDTO(String title, LocalDate publicationDate) {

    public BookInAuthorDTO(Book book) {
        this(
                book.getTitle(),
                book.getPublicationDate());
    }
}
