package ru.springproject.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.springproject.utils.UniqueEmail;

import java.util.List;

@Entity
@Table(name = "market_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{errors.user.not_empty}")
    private String name;

    @NotBlank(message = "{errors.user.not_empty}")
    @Email(message = "{errors.user.email_format}")
    @UniqueEmail(message = "{errors.user.email_unique}")
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "owner")
    private List<Order> ordersList;
}
