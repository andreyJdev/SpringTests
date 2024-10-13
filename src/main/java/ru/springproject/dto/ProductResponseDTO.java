package ru.springproject.dto;

import com.fasterxml.jackson.annotation.JsonView;
import ru.springproject.models.Product;
import ru.springproject.utils.Views;

public record ProductResponseDTO(
        @JsonView(Views.FullDetailWithId.class)
        Long id,

        @JsonView(Views.OrderDetail.class)
        String name,

        @JsonView(Views.OrderDetail.class)
        String description,

        @JsonView(Views.OrderDetail.class)
        Integer price,

        @JsonView(Views.FullDetail.class)
        Integer quantityInStock) {

    public ProductResponseDTO(Product product) {
        this(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getQuantityInStock());
    }
}
