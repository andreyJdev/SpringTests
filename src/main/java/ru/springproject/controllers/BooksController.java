package ru.springproject.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.springproject.dto.AuthorResponseDTO;
import ru.springproject.dto.BookCreateRequestDTO;
import ru.springproject.dto.BookResponseDTO;
import ru.springproject.dto.BookUpdateRequestDTO;
import ru.springproject.services.BooksService;
import ru.springproject.utils.BookNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/books")
@RequiredArgsConstructor
public class BooksController {

    private final BooksService booksService;

    @GetMapping
    public ResponseEntity<Iterable<BookResponseDTO>> getAllBooks() {
        return ResponseEntity.ok().body(booksService.findAllBooks());
    }

    @GetMapping("page")
    public ResponseEntity<Iterable<BookResponseDTO>> getBooksPage(@RequestParam(defaultValue = "0") int offset,
                                                              @RequestParam(defaultValue = "100") int pageSize,
                                                              @RequestParam(defaultValue = "id") String field,
                                                              @RequestParam(defaultValue = "asc") String direction) {
        List<BookResponseDTO> books = booksService.findBooksPage(offset, pageSize, field, direction);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("{bookId:\\d+}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable("bookId") Long id) {
        BookResponseDTO response = this.booksService.findById(id)
                .orElseThrow(() -> new BookNotFoundException("errors.book.not_found"));

        return ResponseEntity.ok(response);
    }

    @PostMapping("create")
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody @Valid BookCreateRequestDTO request,
                                                      BindingResult bindingResult,
                                                      UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        BookResponseDTO response = booksService.createBook(request);

        return ResponseEntity
                .created(uriComponentsBuilder.replacePath("api/v1/books/{bookId}")
                        .build(Map.of("bookId", response.id()))).body(response);
    }

    @PutMapping("{bookId:\\d+}")
    public ResponseEntity<BookResponseDTO> updateBook(@PathVariable("bookId") Long id,
                                                      @RequestBody @Valid BookUpdateRequestDTO request,
                                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return ResponseEntity.ok(booksService.updateBook(id, request));
    }

    @DeleteMapping("{bookId:\\d+}")
    public ResponseEntity<BookResponseDTO> deleteBook(@PathVariable("bookId") Long id) {
        if (this.booksService.deleteBook(id) == 0) {
            throw new BookNotFoundException("errors.book.not_found");
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}