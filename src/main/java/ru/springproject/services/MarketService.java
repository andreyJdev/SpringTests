package ru.springproject.services;

import ru.springproject.dto.OrderRequestDTO;
import ru.springproject.dto.OrderResponseDTO;
import ru.springproject.dto.UserResponseDTO;
import ru.springproject.models.Order;
import ru.springproject.models.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface MarketService {

    List<User> findAllUsers();

    Map<UserResponseDTO, Set<OrderResponseDTO>> findUserByIdWithOrder(Long id);

    User saveUser(User newUser);

    int deleteUser(Long id);
    // в разработке (должен быть убран в отдельный service)
    void createOrder(OrderRequestDTO request);
}
