package ru.springproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.springproject.dto.BookCreateRequestDTO;
import ru.springproject.dto.BookUpdateRequestDTO;
import ru.springproject.utils.Genres;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Library_book")
public class Book {

    public Book(BookCreateRequestDTO createRequest, Set<Author> authors) {
        this.id = null;
        this.title = createRequest.title();
        this.genre = createRequest.genre();
        this.publicationDate = createRequest.publicationDate();
        this.authors = authors;
    }

    public Book(Long id, BookUpdateRequestDTO updateRequest) {
        this.id = id;
        this.title = updateRequest.title();
        this.genre = updateRequest.genre();
        this.publicationDate = updateRequest.publicationDate();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookId")
    private Long id;

    @NotEmpty(message = "{errors.book.title_is_empty}")
    @Size(min = 2, max = 32, message = "{errors.book.title_size_invalid}")
    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genres genre;

    @NotNull(message = "{errors.book.date_is_null}")
    @Past(message = "{errors.book.date_is_invalid}")
    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Author> authors = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && genre == book.genre && Objects.equals(publicationDate, book.publicationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, genre, publicationDate);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", genre=" + genre +
                ", publicationDate=" + publicationDate +
                '}';
    }
}
