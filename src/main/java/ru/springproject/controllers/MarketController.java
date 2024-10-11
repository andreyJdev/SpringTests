package ru.springproject.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.springproject.dto.OrderRequestDTO;
import ru.springproject.dto.OrderResponseDTO;
import ru.springproject.dto.UserResponseDTO;
import ru.springproject.models.User;
import ru.springproject.services.MarketService;
import ru.springproject.utils.UserNotFoundException;
import ru.springproject.utils.Views;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @JsonView(Views.UserSummeryWithId.class)
    @GetMapping("users")
    public ResponseEntity<Iterable<User>> getUsers() {
        return ResponseEntity.ok()
                .body(this.marketService.findAllUsers());
    }

    @JsonView(Views.UserDetails.class)
    @GetMapping("users/{userId:\\d+}")
    public ResponseEntity<Map<UserResponseDTO, Set<OrderResponseDTO>>> getUser(@PathVariable("userId") Long id) {
        Map<UserResponseDTO, Set<OrderResponseDTO>> user = this.marketService.findUserByIdWithOrder(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("create")
    public ResponseEntity<User> createUser(@Valid @RequestBody User newUser,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        newUser = this.marketService.saveUser(newUser);
        return ResponseEntity.created(uriComponentsBuilder
                .replacePath("api/v1/users/{userId}")
                .build(Map.of("userId", newUser.getId())))
                .body(newUser);
    }

    @PatchMapping("update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user,
                                           BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        user = this.marketService.saveUser(user);

        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("users/{userId:\\d+}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long id) {

        boolean deleted = this.marketService.deleteUser(id) > 0;

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new UserNotFoundException("errors.user.not_found");
        }
    }
    // в разработке
    @PostMapping("orders/create")
    public void createOrder(@RequestBody OrderRequestDTO order) {
        marketService.createOrder(order);
    }
}
