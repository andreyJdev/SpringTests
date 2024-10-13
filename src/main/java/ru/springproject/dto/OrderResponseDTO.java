package ru.springproject.dto;

import com.fasterxml.jackson.annotation.JsonView;
import ru.springproject.models.Customer;
import ru.springproject.models.Order;
import ru.springproject.models.Product;
import ru.springproject.utils.OrderStatus;
import ru.springproject.utils.Views;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(

        @JsonView(Views.FullDetailWithId.class)
        Long id,

        @JsonView(Views.OrderDetail.class)
        LocalDateTime orderDate,

        @JsonView(Views.OrderDetail.class)
        Integer totalPrice,

        @JsonView(Views.OrderDetail.class)
        String shippingAddress,

        @JsonView(Views.OrderDetail.class)
        OrderStatus orderStatus,

        @JsonView(Views.OrderDetail.class)
        List<Product> products,

        @JsonView(Views.OrderDetail.class)
        Customer owner) {


    public OrderResponseDTO(Order order) {
        this(order.getId(),
                order.getOrderDate(),
                order.getTotalPrice(),
                order.getShippingAddress(),
                order.getOrderStatus(),
                order.getProducts(),
                order.getOwner());
    }
}
