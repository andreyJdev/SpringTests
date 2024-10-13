package ru.springproject.models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.springproject.dto.OrderCreateRequestDTO;
import ru.springproject.utils.OrderStatus;
import ru.springproject.utils.Views;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "market_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    public Order(OrderCreateRequestDTO requestCreate) {
        this.id = null;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = null;
        this.shippingAddress = requestCreate.shippingAddress();
        this.orderStatus = OrderStatus.SHIPPED;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullDetailWithId.class)
    @Column(name = "order_id")
    private Long id;

    @NotNull
    @JsonView(Views.FullDetail.class)
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @NotNull
    @JsonView(Views.FullDetail.class)
    @Column(name = "total_price")
    private Integer totalPrice;

    @NotEmpty
    @JsonView(Views.FullDetail.class)
    @Column(name = "shipping_address")
    private String shippingAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "market_order_product",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonView(Views.FullDetail.class)
    private List<Product> products = new ArrayList<>();

    @JsonView(Views.Internal.class)
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer owner;
}
