package ru.springproject.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.springproject.models.User;
import ru.springproject.services.MarketService;
import ru.springproject.utils.UserNotFoundException;

import java.util.Map;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;

    @GetMapping("users")
    public ResponseEntity<Iterable<User>> getUsers() {
        return ResponseEntity.ok()
                .body(marketService.findAllUsers());
    }

    @GetMapping("users/{userId:\\d+}")
    public ResponseEntity<User> getUser(@PathVariable("userId") Long id) {
        return ResponseEntity.ok().body(marketService.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException("errors.user.not_found")));
    }

    @PostMapping("create")
    public ResponseEntity<User> createUser(@Valid @RequestBody User newUser,
                                           BindingResult bindingResult,
                                           UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        newUser = marketService.saveUser(newUser);
        return ResponseEntity.created(uriComponentsBuilder
                .replacePath("api/v1/users/{userId}")
                .build(Map.of("userId", newUser.getId())))
                .body(newUser);
    }

    @PatchMapping("users/{userId:\\d+}")
    public ResponseEntity<User> updateUser() {
        return null;
    }
}
