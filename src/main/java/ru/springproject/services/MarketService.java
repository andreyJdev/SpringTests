package ru.springproject.services;

import ru.springproject.dto.*;
import ru.springproject.models.Order;

import java.util.Optional;
import java.util.Set;

public interface MarketService {


    OrderResponseDTO createOrder(OrderCreateRequestDTO request);

    Optional<Set<ProductResponseDTO>> findAllProducts();

    Optional<ProductResponseDTO> findProductById(Long id);

    ProductResponseDTO createProduct(ProductCreateRequestDTO request);

    ProductResponseDTO updateProduct(Long id, ProductUpdateRequestDTO request);

    int deleteProduct(Long id);

    Optional<OrderResponseDTO> findOrderById(Long id);
}
