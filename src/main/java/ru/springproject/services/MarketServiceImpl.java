package ru.springproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springproject.dto.OrderRequestDTO;
import ru.springproject.dto.OrderResponseDTO;
import ru.springproject.dto.UserResponseDTO;
import ru.springproject.models.Order;
import ru.springproject.models.Product;
import ru.springproject.models.User;
import ru.springproject.repositories.OrdersRepository;
import ru.springproject.repositories.ProductsRepository;
import ru.springproject.repositories.UsersRepository;
import ru.springproject.utils.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketServiceImpl implements MarketService {

    private final ProductsRepository productsRepository;
    private final OrdersRepository ordersRepository;
    private final UsersRepository usersRepository;

    public List<User> findAllUsers() {
        return this.usersRepository.findAll();
    }

    public Map<UserResponseDTO, Set<OrderResponseDTO>> findUserByIdWithOrder(Long id) {
        User user = this.usersRepository.findByIdWithOrder(id)
                .orElseThrow(() -> new UserNotFoundException("errors.user.not_found"));

        Set<OrderResponseDTO> orders = user.getOrdersList().stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toSet());

        return Collections.singletonMap(new UserResponseDTO(user), orders);
    }

    @Transactional
    public User saveUser(User newUser) {
        newUser.setEmail(newUser.getEmail().toLowerCase());
        return this.usersRepository.save(newUser);
    }

    @Transactional
    public int deleteUser(Long id) {
        return this.usersRepository.removeById(id);
    }

    // в разработке
    @Transactional
    public void createOrder(OrderRequestDTO request) {
        if (request == null || request.getProductsIds() == null || request.getProductsIds().isEmpty()) {
            throw new RuntimeException();
        }
        Order order = new Order();

        order.setProducts(this.productsRepository.findByIdIn(new ArrayList<>(request.getProductsIds())));
        order.setOwner(this.usersRepository
                .findById(request.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException("errors.user.not_found")));

        order.setId(null);
        order.setOrderDate(LocalDateTime.now());

        int totalPrice = order.getProducts()
                .stream()
                .mapToInt(Product::getPrice)
                .sum();
        order.setTotalPrice(totalPrice);

        this.ordersRepository.save(order);
    }
}
