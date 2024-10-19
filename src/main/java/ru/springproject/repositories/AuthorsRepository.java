package ru.springproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.springproject.models.Author;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AuthorsRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Author a JOIN FETCH a.books")
    Set<Author> findAllAuthors();

    @Query("SELECT a FROM Author a JOIN FETCH a.books WHERE a.id = :id")
    Optional<Author> findAuthorById(@Param("id") Long id);

    @Query("SELECT a FROM Author a WHERE a.id IN :ids")
    Set<Author> findByIds(@Param("ids") Set<Long> ids);

    int removeById(Long id);
}
