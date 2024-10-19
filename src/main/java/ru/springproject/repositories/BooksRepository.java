package ru.springproject.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.springproject.models.Book;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b JOIN FETCH b.authors")
    Set<Book> findAllBooks();

    @Query("SELECT b.id FROM Book b")
    Page<Long> findBooksPageIds(Pageable pageable);

    @Query("SELECT b FROM Book b JOIN FETCH b.authors WHERE b.id IN :ids")
    List<Book> findBooksPageByIds(@Param("ids") List<Long> ids);

    @Query("SELECT b FROM Book b JOIN FETCH b.authors WHERE b.id = :id")
    Optional<Book> findBookById(@Param("id") Long id);

    @Query("SELECT b FROM Book b WHERE b.id IN :ids")
    Set<Book> findBooksByIds(@Param("ids") Set<Long> ids);

    int removeById(Long id);
}
