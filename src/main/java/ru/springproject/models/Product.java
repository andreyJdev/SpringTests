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
import ru.springproject.dto.ProductCreateRequestDTO;
import ru.springproject.dto.ProductUpdateRequestDTO;
import ru.springproject.utils.Views;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "market_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    public Product(ProductCreateRequestDTO createRequest) {
        this.id = null;
        this.name = createRequest.name();
        this.description = createRequest.description();
        this.price = createRequest.price();
        this.quantityInStock = createRequest.quantityInStock();
        this.orders = new ArrayList<>();
    }

    public Product(Long id, ProductUpdateRequestDTO updateRequest) {
        this.id = id;
        this.name = updateRequest.name();
        this.description = updateRequest.description();
        this.price = updateRequest.price();
        this.quantityInStock = updateRequest.quantityInStock();
        this.orders = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullDetailWithId.class)
    @Column(name = "product_id")
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 32, message = "Name must be between 2 and 15 characters")
    @JsonView(Views.OrderDetail.class)
    @Column(name = "name")
    private String name;

    @JsonView(Views.FullDetail.class)
    @Size(max = 128, message = "Description must be lower than 128 characters")
    @Column(name = "description")
    private String description;

    @JsonView(Views.OrderDetail.class)
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be at positive")
    @Column(name = "price")
    private Integer price;

    @JsonView(Views.FullDetail.class)
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Quantity must be at positive")
    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;

    @ManyToMany(mappedBy = "products")
    @JsonView(Views.Internal.class)
    private List<Order> orders;
}
