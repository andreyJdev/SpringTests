package ru.springproject.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.springproject.models.Order;
import ru.springproject.models.Product;
import ru.springproject.utils.Views;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderResponseDTO {

    @JsonView(Views.Internal.class)
    private Long id;

    @JsonView(Views.UserDetails.class)
    private LocalDateTime orderDate;

    @JsonView(Views.UserDetails.class)
    private Integer totalPrice;

    @JsonView(Views.UserDetails.class)
    private List<Product> products;

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.orderDate = order.getOrderDate();
        this.totalPrice = order.getTotalPrice();
        this.products = order.getProducts();
    }

    @Override
    public String toString() {
        return "OrderResponseDTO{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", totalPrice=" + totalPrice +
                ", products=" + products +
                '}';
    }
}
