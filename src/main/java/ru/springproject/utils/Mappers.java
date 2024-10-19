package ru.springproject.utils;

import org.springframework.stereotype.Component;
import ru.springproject.dto.AuthorUpdateRequestDTO;
import ru.springproject.dto.BookUpdateRequestDTO;
import ru.springproject.models.Author;
import ru.springproject.models.Book;

@Component
public class Mappers {

    public void mapUpdateRequestToBook(BookUpdateRequestDTO request, Book book) {
        book.setTitle(request.title());
        book.setGenre(request.genre());
        book.setPublicationDate(request.publicationDate());
    }

    public void mapUpdateRequestToAuthor(AuthorUpdateRequestDTO request, Author author) {
        author.setName(request.name());
        author.setDateOfBirth(request.dateOfBirth());
    }
}
