package ru.springproject.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequestDTO(

        @Size(min = 2, max = 32, message = "Name must be between 2 and 15 characters")
        String name,

        @Size(max = 128, message = "Description must be lower than 128 characters")
        String description,

        @NotNull(message = "Price cannot be null")
        @Positive(message = "Price must be at positive")
        Integer price,

        @NotNull(message = "Price cannot be null")
        @Positive(message = "Quantity must be at positive")
        Integer quantityInStock) {
}
