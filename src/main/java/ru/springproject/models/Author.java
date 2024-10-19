package ru.springproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.springproject.dto.AuthorCreateRequestDTO;
import ru.springproject.dto.AuthorUpdateRequestDTO;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Library_author")
public class Author {

    public Author(AuthorCreateRequestDTO createRequest, Set<Book> books) {
        this.name = createRequest.name();
        this.dateOfBirth = createRequest.dateOfBirth();
        this.books = books;
    }

    public Author(AuthorUpdateRequestDTO updateRequest) {
        this.name = updateRequest.name();
        this.dateOfBirth = updateRequest.dateOfBirth();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;

    @Size(min = 2, max = 32, message = "{errors.author.name_size_invalid}")
    @NotNull(message = "{errors.author.name_is_null}")
    @Column(name = "name")
    private String name;

    @NotNull(message = "{errors.author.date_is_null}")
    @Past(message = "{errors.author.date_is_invalid}")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @JoinTable(
            name = "library_author_book",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private Set<Book> books = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(name, author.name) && Objects.equals(dateOfBirth, author.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
