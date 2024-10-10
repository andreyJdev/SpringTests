package ru.springproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.springproject.models.User;
import ru.springproject.repositories.OrdersRepository;
import ru.springproject.repositories.ProductsRepository;
import ru.springproject.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;


@Validated
@Service
@RequiredArgsConstructor
public class MarketServiceImpl implements MarketService {

    private final ProductsRepository productsRepository;
    private final OrdersRepository ordersRepository;
    private final UsersRepository usersRepository;

    public List<User> findAllUsers() {
        return usersRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        return usersRepository.findById(id);
    }

    public User saveUser(User newUser) {
        return usersRepository.save(newUser);
    }
}
