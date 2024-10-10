package ru.springproject.services;

import ru.springproject.models.User;

import java.util.List;
import java.util.Optional;

public interface MarketService {

    List<User> findAllUsers();

    Optional<User> findUserById(Long id);

    User saveUser(User newUser);
}
