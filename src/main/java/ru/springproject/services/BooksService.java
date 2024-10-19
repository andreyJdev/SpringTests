package ru.springproject.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.springproject.dto.BookCreateRequestDTO;
import ru.springproject.dto.BookResponseDTO;
import ru.springproject.dto.BookUpdateRequestDTO;

import java.util.List;
import java.util.Optional;

public interface BooksService {

    List<BookResponseDTO> findAllBooks();

    public List<BookResponseDTO> findBooksPage(int offset, int pageSize, String sortField, String sortDirection);

    Optional<BookResponseDTO> findById(Long id);

    BookResponseDTO createBook(BookCreateRequestDTO bookCreateRequestDTO);

    BookResponseDTO updateBook(Long id, BookUpdateRequestDTO bookUpdateRequestDTO);

    int deleteBook(Long id);
}
