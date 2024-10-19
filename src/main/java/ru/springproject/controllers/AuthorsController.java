package ru.springproject.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.springproject.dto.AuthorCreateRequestDTO;
import ru.springproject.dto.AuthorResponseDTO;
import ru.springproject.dto.AuthorUpdateRequestDTO;
import ru.springproject.services.AuthorsService;
import ru.springproject.utils.AuthorNotFoundException;

import java.util.Map;

@RestController
@RequestMapping("api/v1/authors")
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorsService authorsService;

    @GetMapping
    public ResponseEntity<Iterable<AuthorResponseDTO>> getAuthors() {
        return ResponseEntity.ok().body(this.authorsService.findAllAuthors());
    }

    @GetMapping("{authorId:\\d+}")
    public ResponseEntity<AuthorResponseDTO> getAuthorById(@PathVariable("authorId") Long id) {
        return ResponseEntity.ok().body(this.authorsService.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("errors.author.not_found")));
    }

    @PostMapping("create")
    public ResponseEntity<AuthorResponseDTO> createAuthor(@RequestBody @Valid AuthorCreateRequestDTO request,
                                                          BindingResult bindingResult,
                                                          UriComponentsBuilder uriBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        AuthorResponseDTO response = this.authorsService.createAuthor(request);

        return ResponseEntity.created(uriBuilder
                        .replacePath("/api/v1/authors/{authorId}")
                        .build(Map.of("authorId", response.id())))
                .body(response);
    }

    @PutMapping("{authorId:\\d+}")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(@RequestBody @Valid AuthorUpdateRequestDTO request,
                                                          @PathVariable("authorId") Long id,
                                                          BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        return ResponseEntity.ok().body(this.authorsService.updateAuthor(id, request));
    }

    @DeleteMapping("{authorId:\\d+}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable("authorId") Long id) {
        if (this.authorsService.deleteAuthor(id) == 0) {
            throw new AuthorNotFoundException("errors.author.not_found");
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
