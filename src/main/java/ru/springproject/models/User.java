package ru.springproject.models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.springproject.utils.UniqueEmail;
import ru.springproject.utils.Views;

import java.util.List;

@Entity
@Table(name = "market_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@UniqueEmail(message = "{errors.user.email_unique}")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.UserSummeryWithId.class)
    private Long id;

    @NotBlank(message = "{errors.user.not_empty}")
    @JsonView(Views.UserSummery.class)
    private String name;

    @NotBlank(message = "{errors.user.not_empty}")
    @Email(message = "{errors.user.email_format}")
    @Column(unique = true)
    @JsonView(Views.UserSummery.class)
    private String email;

    @OneToMany(mappedBy = "owner")
    @JsonView(Views.UserDetails.class)
    private List<Order> ordersList;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
