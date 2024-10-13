package ru.springproject.models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.springproject.utils.Views;

import java.util.List;

@Entity
@Table(name = "market_customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.FullDetailWithId.class)
    @Column(name = "customer_id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 15)
    @JsonView(Views.OrderDetail.class)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 15)
    @JsonView(Views.OrderDetail.class)
    @Column(name = "last_name")
    private String lastName;

    @NotNull
    @Size(min = 5, max = 30)
    @JsonView(Views.OrderDetail.class)
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Pattern(regexp = "\\+\\d{1,3}\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}", message = "Invalid phone number format")
    @JsonView(Views.OrderDetail.class)
    @Column(name = "contact_number", unique = true)
    private String contactNumber;

    @OneToMany(mappedBy = "owner")
    @JsonView(Views.Internal.class)
    private List<Order> ordersList;
}
