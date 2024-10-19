package ru.springproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springproject.dto.*;
import ru.springproject.models.Author;
import ru.springproject.models.Book;
import ru.springproject.repositories.AuthorsRepository;
import ru.springproject.repositories.BooksRepository;
import ru.springproject.utils.AuthorNotFoundException;
import ru.springproject.utils.BookNotFoundException;
import ru.springproject.utils.Mappers;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorsServiceImpl implements AuthorsService {

    private final AuthorsRepository authorsRepository;
    private final BooksRepository booksRepository;
    private final Mappers mapper;

    public List<AuthorResponseDTO> findAllAuthors() {
        List<Author> authors = new ArrayList<>(authorsRepository.findAllAuthors());
        authors.sort(Comparator.comparing(Author::getId));

        if (authors.isEmpty()) {
            throw new AuthorNotFoundException("errors.authors.not_found");
        }
        return authors
                .stream()
                .map(AuthorResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<AuthorResponseDTO> findById(Long id) {
        return authorsRepository.findAuthorById(id)
                .map(AuthorResponseDTO::new);
    }

    @Transactional
    public AuthorResponseDTO createAuthor(AuthorCreateRequestDTO createRequest) {
        Set<Book> books = booksRepository.findBooksByIds(createRequest.books());

        if (books == null || books.isEmpty()) {
            throw new BookNotFoundException("errors.books.not_found");
        }

        Author author = new Author(createRequest, books);

        books.forEach(book -> book.getAuthors().add(author));

        return new AuthorResponseDTO(this.authorsRepository.save(author));
    }

    @Transactional
    public AuthorResponseDTO updateAuthor(Long id, AuthorUpdateRequestDTO updateRequest) {
        Set<Book> books = this.booksRepository.findBooksByIds(updateRequest.books());

        if (books == null || books.isEmpty()) {
            throw new BookNotFoundException("errors.books.not_found");
        }

        Author author = this.authorsRepository.findAuthorById(id)
                .orElseThrow(() -> new AuthorNotFoundException("errors.author.not_found"));

        author.getBooks().forEach(book -> {
            if (!books.contains(book)) {
                book.getAuthors().remove(author);
            }
        });

        books.forEach(book -> {
            if (!author.getBooks().contains(book)) {
                book.getAuthors().add(author);
            }
        });

        this.mapper.mapUpdateRequestToAuthor(updateRequest, author);
        author.setId(id);
        author.setBooks(books);

        return new AuthorResponseDTO(this.authorsRepository.save(author));
    }

    @Transactional
    public int deleteAuthor(Long id) {
        Author authorForDelete = this.authorsRepository.findAuthorById(id)
                .orElseThrow(() -> new AuthorNotFoundException("errors.author.not_found"));

        authorForDelete
                .getBooks()
                .forEach(book ->
                        book.getAuthors().remove(authorForDelete));

        return this.authorsRepository.removeById(id);
    }
}
