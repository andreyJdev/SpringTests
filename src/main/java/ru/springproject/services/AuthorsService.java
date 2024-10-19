package ru.springproject.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.springproject.dto.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthorsService {
    List<AuthorResponseDTO> findAllAuthors();

    Optional<AuthorResponseDTO> findById(Long id);

    AuthorResponseDTO createAuthor(AuthorCreateRequestDTO createRequest);

    AuthorResponseDTO updateAuthor(Long id, AuthorUpdateRequestDTO updateRequest);

    int deleteAuthor(Long id);
}
