package ru.springproject.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.springproject.models.User;
import ru.springproject.utils.Views;

@Data
@NoArgsConstructor
public class UserResponseDTO {

    @JsonView(Views.UserSummeryWithId.class)
    private Long id;

    @JsonView(Views.UserSummery.class)
    private String name;

    @JsonView(Views.UserSummery.class)
    private String email;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
