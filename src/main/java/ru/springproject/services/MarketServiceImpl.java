package ru.springproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.springproject.dto.*;
import ru.springproject.models.Order;
import ru.springproject.models.Product;
import ru.springproject.repositories.OrdersRepository;
import ru.springproject.repositories.ProductsRepository;
import ru.springproject.repositories.UsersRepository;
import ru.springproject.utils.CustomerNotFoundException;
import ru.springproject.utils.ProductNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MarketServiceImpl implements MarketService {

    private final ProductsRepository productsRepository;
    private final OrdersRepository ordersRepository;
    private final UsersRepository usersRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderCreateRequestDTO request) {
        if (request == null || request.productsIds() == null || request.productsIds().isEmpty()) {
            throw new RuntimeException();
        }
        Order order = new Order(request);

        order.setProducts(
                recalculateQuantityAndSaveProducts(this.productsRepository
                                .findByIdIn(new ArrayList<>(request.productsIds()))));

        order.setOwner(this.usersRepository
                .findById(request.ownerId())
                .orElseThrow(() -> new CustomerNotFoundException("User not found")));

        int totalPrice = order.getProducts()
                .stream()
                .mapToInt(Product::getPrice)
                .sum();
        order.setTotalPrice(totalPrice);

        return new OrderResponseDTO(this.ordersRepository.save(order));
    }

    public Optional<Set<ProductResponseDTO>> findAllProducts() {
        return Optional.of(this.productsRepository
                .findAll()
                .stream()
                .map(ProductResponseDTO::new)
                .collect(Collectors.toSet()));
    }

    public Optional<ProductResponseDTO> findProductById(Long id) {
        return this.productsRepository.findById(id).map(ProductResponseDTO::new);
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductCreateRequestDTO request) {
        return new ProductResponseDTO(
                this.productsRepository
                        .save(new Product(request)));
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, ProductUpdateRequestDTO request) {
        Product savedProduct = this.productsRepository.findById(id)
                .map(existingProduct -> this.productsRepository.save(new Product(id, request)))
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        return new ProductResponseDTO(savedProduct);
    }

    @Transactional
    public int deleteProduct(Long id) {
        return this.productsRepository.removeById(id);
    }

    @Override
    public Optional<OrderResponseDTO> findOrderById(Long id) {
        return this.ordersRepository.findById(id).map(OrderResponseDTO::new);
    }

    public List<Product> recalculateQuantityAndSaveProducts(List<Product> products) {
        Map<Long, Product> productMap = new HashMap<>();
        products.forEach(product -> {
            productMap.putIfAbsent(product.getId(), product);
            Product existingProduct = productMap.get(product.getId());
            existingProduct.setQuantityInStock(existingProduct.getQuantityInStock() - 1);
        });
        this.productsRepository.saveAll(productMap.values());
        return products;
    }
}
