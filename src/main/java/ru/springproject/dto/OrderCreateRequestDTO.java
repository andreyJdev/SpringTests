package ru.springproject.dto;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.springproject.models.Customer;
import ru.springproject.models.Product;
import ru.springproject.utils.OrderStatus;
import ru.springproject.utils.Views;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record OrderCreateRequestDTO(
        @NotEmpty
        @JsonView(Views.OrderDetail.class)
        @Column(name = "shipping_address")
        String shippingAddress,

        @JsonView(Views.FullDetail.class)
        @NotEmpty
        @NotNull
        List<Long> productsIds,

        @NotNull
        Long ownerId) { }
