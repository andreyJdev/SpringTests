package ru.springproject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.springproject.dto.*;
import ru.springproject.models.Customer;
import ru.springproject.models.Order;
import ru.springproject.models.Product;
import ru.springproject.repositories.OrdersRepository;
import ru.springproject.repositories.ProductsRepository;
import ru.springproject.repositories.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketServiceImplTest {

    @Mock
    ProductsRepository productsRepository;

    @Mock
    OrdersRepository ordersRepository;

    @Mock
    UsersRepository usersRepository;

    @InjectMocks
    MarketServiceImpl marketService;

    @Test
    void createOrder_ValidRequest_CreatesOrder() {
        // given
        OrderCreateRequestDTO request = new OrderCreateRequestDTO("123 Main St", List.of(1L, 2L), 1L);

        List<Product> products = List
                .of(new Product(1L, "Product 1", "Desc", 100, 10, new ArrayList<>()),
                        new Product(2L, "Product 2", "Desc", 200, 20, new ArrayList<>()));
        Customer user = new Customer(1L, "John", "Doe", "john@example.com", "+7(932)492-32-32", new ArrayList<>());
        Order savedOrder = new Order();

        when(productsRepository.findByIdIn(anyList())).thenReturn(products);
        when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ordersRepository.save(any(Order.class))).thenReturn(savedOrder);

        // when
        OrderResponseDTO response = marketService.createOrder(request);

        // then
        assertNotNull(response);
        verify(productsRepository).findByIdIn(anyList());
        verify(usersRepository).findById(1L);
        verify(ordersRepository).save(any(Order.class));
    }

    @Test
    void findAllProducts_ReturnProducts() {
        // given
        List<Product> products = List
                .of(new Product(1L, "Product 1", "Desc", 100, 10, new ArrayList<>()),
                        new Product(2L, "Product 2", "Desc", 200, 20, new ArrayList<>()));
        when(productsRepository.findAll()).thenReturn(products);

        // when
        Optional<Set<ProductResponseDTO>> result = marketService.findAllProducts();

        // then
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        verify(productsRepository).findAll();
    }

    @Test
    void findProductById_ExistingProduct_ReturnProduct() {
        // given
        Product product = new Product(1L, "Product 1", "Desc", 100, 10, new ArrayList<>());
        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        Optional<ProductResponseDTO> result = marketService.findProductById(1L);

        // then
        assertTrue(result.isPresent());
        assertEquals("Product 1", result.get().name());
        verify(productsRepository).findById(1L);
    }

    @Test
    void createProduct_ValidRequest_SavesProduct() {
        // given
        ProductCreateRequestDTO request = new ProductCreateRequestDTO("Product 1", "Description", 100, 10);
        Product savedProduct = new Product(request);
        when(productsRepository.save(any(Product.class))).thenReturn(savedProduct);

        // when
        ProductResponseDTO result = marketService.createProduct(request);

        // then
        assertNotNull(result);
        assertEquals("Product 1", result.name());
        verify(productsRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_ExistingProduct_UpdatesProduct() {
        // given
        ProductUpdateRequestDTO request = new ProductUpdateRequestDTO("Updated Product", "Updated Description", 200, 20);
        Product existingProduct = new Product(1L, "Product 1", "Desc", 100, 10, new ArrayList<>());
        Product savedProduct = new Product(1L, request);

        when(productsRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productsRepository.save(any(Product.class))).thenReturn(savedProduct);

        // when
        ProductResponseDTO result = marketService.updateProduct(1L, request);

        // then
        assertNotNull(result);
        assertEquals("Updated Product", result.name());
        verify(productsRepository).findById(1L);
        verify(productsRepository).save(any(Product.class));
    }

    @Test
    void deleteProduct_ExistingProduct_RemovesProduct() {
        // given
        when(productsRepository.removeById(1L)).thenReturn(1);

        // when
        int result = marketService.deleteProduct(1L);

        // then
        assertEquals(1, result);
        verify(productsRepository).removeById(1L);
    }

    @Test
    void findOrderById_ExistingOrder_ReturnOrder() {
        // given
        Order order = new Order();
        when(ordersRepository.findById(1L)).thenReturn(Optional.of(order));

        // when
        Optional<OrderResponseDTO> result = marketService.findOrderById(1L);

        // then
        assertTrue(result.isPresent());
        verify(ordersRepository).findById(1L);
    }

    @Test
    void recalculateQuantityAndSaveProducts_ProductsUpdated() {
        // given
        List<Product> products = List.of(
                new Product(1L, "Product 1", "Desc", 100, 10, new ArrayList<>()),
                new Product(2L, "Product 2", "Desc", 200, 20, new ArrayList<>())
        );

        // when
        doReturn(products).when(productsRepository).saveAll(anyCollection());

        // then
        List<Product> result = marketService.recalculateQuantityAndSaveProducts(products);
        assertEquals(2, result.size());
        assertEquals(9, result.get(0).getQuantityInStock()); // Уменьшили на 2
        assertEquals(19, result.get(1).getQuantityInStock()); // Уменьшили на 2
        verify(productsRepository).saveAll(anyCollection());
    }
}

