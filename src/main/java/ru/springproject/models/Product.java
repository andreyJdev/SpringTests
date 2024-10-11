package ru.springproject.models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.springproject.utils.Views;

import java.util.List;

@Entity
@Table(name = "market_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Internal.class)
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 15)
    @JsonView(Views.UserDetails.class)
    private String name;

    @NotNull
    @Positive
    @JsonView(Views.UserDetails.class)
    private Integer price;
    @JsonView(Views.UserDetails.class)
    private String description;

    @ManyToMany(mappedBy = "products")
    @JsonView(Views.Internal.class)
    private List<Order> orders;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
