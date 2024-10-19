package ru.springproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springproject.dto.BookCreateRequestDTO;
import ru.springproject.dto.BookResponseDTO;
import ru.springproject.dto.BookUpdateRequestDTO;
import ru.springproject.models.Author;
import ru.springproject.models.Book;
import ru.springproject.repositories.AuthorsRepository;
import ru.springproject.repositories.BooksRepository;
import ru.springproject.utils.AuthorNotFoundException;
import ru.springproject.utils.BookNotFoundException;
import ru.springproject.utils.Mappers;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BooksServiceImpl implements BooksService {

    private final BooksRepository booksRepository;
    private final AuthorsRepository authorsRepository;
    private final Mappers mapper;

    public List<BookResponseDTO> findAllBooks() {
        List<Book> books = new ArrayList<>(this.booksRepository.findAllBooks());
        books.sort(Comparator.comparing(Book::getId));

        if (books.isEmpty()) {
            throw new BookNotFoundException("errors.books.not_found");
        }
        return books
                .stream()
                .map(BookResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<BookResponseDTO> findBooksPage(int offset, int pageSize, String field, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("desc") ? Sort.by(Sort.Direction.DESC, field) : Sort.by(Sort.Direction.ASC, field);
        Pageable pageable = PageRequest.of(offset, pageSize, sort);

        Page<Long> pageIds = this.booksRepository.findBooksPageIds(pageable);
        if (pageIds == null || pageIds.getTotalElements() == 0) {
            throw new BookNotFoundException("errors.books.not_found");
        }

        Map<Long, Book> books = this.booksRepository.findBooksPageByIds(pageIds.getContent())
                .stream()
                .collect(Collectors.toMap(
                        Book::getId,
                        Function.identity(),
                        (oldValue, newValue) -> oldValue,
                        HashMap::new
                ));

        return pageIds
                .stream()
                .map(id -> new BookResponseDTO(books.get(id)))
                .toList();
    }

    public Optional<BookResponseDTO> findById(Long id) {
        return this.booksRepository.findBookById(id).map(BookResponseDTO::new);
    }

    @Transactional
    public BookResponseDTO createBook(BookCreateRequestDTO bookCreateRequestDTO) {
        Set<Author> authors = this.authorsRepository.findByIds(bookCreateRequestDTO.authors());
        Book createdBook = new Book(bookCreateRequestDTO, authors);

        if (authors == null || authors.isEmpty()) {
            throw new AuthorNotFoundException("errors.author.not_found");
        }

        createdBook.setAuthors(authors);

        authors.forEach(author -> author.getBooks().add(createdBook));

        return new BookResponseDTO(this.booksRepository.save(createdBook));
    }


    @Transactional
    public BookResponseDTO updateBook(Long id, BookUpdateRequestDTO bookUpdateRequestDTO) {
        Set<Author> authors = this.authorsRepository.findByIds(bookUpdateRequestDTO.authors());
        if (authors == null || authors.isEmpty()) {
            throw new AuthorNotFoundException("errors.author.not_found");
        }

        return this.booksRepository.findById(id)
                .map(book -> {
                    if (!book.getAuthors().equals(authors)) {
                        book.getAuthors().forEach(author -> {
                            if (!authors.contains(author)) {
                                author.getBooks().remove(book);
                            }
                        });
                        authors.forEach(author -> {
                            if (!book.getAuthors().contains(author)) {
                                author.getBooks().add(book);
                            }
                        });
                    }

                    mapper.mapUpdateRequestToBook(bookUpdateRequestDTO, book);
                    book.setId(id);
                    book.setAuthors(authors);
                    return new BookResponseDTO(this.booksRepository.save(book));
                })
                .orElseThrow(() -> new BookNotFoundException("errors.book.not_found"));
    }

    @Transactional
    public int deleteBook(Long id) {
        Book bookForDelete = this.booksRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("errors.book.not_found"));

        bookForDelete.getAuthors().forEach(author -> {
            author.getBooks().remove(bookForDelete);
        });

        return this.booksRepository.removeById(id);
    }
}