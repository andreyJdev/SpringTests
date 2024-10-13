package ru.springproject.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.springproject.dto.*;
import ru.springproject.services.MarketService;
import ru.springproject.utils.OrderNotFoundException;
import ru.springproject.utils.ProductNotFoundException;
import ru.springproject.utils.Views;

import java.util.Map;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class MarketController {

    private final MarketService marketService;
    private final ObjectMapper objectMapper;

    @GetMapping("products")
    @JsonView(Views.FullDetailWithId.class)
    public ResponseEntity<String> getAllProducts() throws JsonProcessingException {
        String productsResponseJson =  objectMapper.writeValueAsString(this.marketService.findAllProducts()
                .orElseThrow(() -> new ProductNotFoundException("No products found")));
        return ResponseEntity
                .ok()
                .body(productsResponseJson);
    }

    @GetMapping("products/{productId:\\d+}")
    @JsonView(Views.FullDetail.class)
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable("productId") Long id) {
        return ResponseEntity
                .ok()
                .body(this.marketService.findProductById(id)
                        .orElseThrow(() -> new ProductNotFoundException("Product not found")));
    }

    @PostMapping("products/create")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductCreateRequestDTO request,
                                                            BindingResult bindingResult,
                                                            UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        ProductResponseDTO createdProduct = this.marketService.createProduct(request);

        return ResponseEntity
                .created(uriComponentsBuilder.replacePath("/api/v1/products/{productId}")
                        .build(Map.of("productId", createdProduct.id())))
                .body(createdProduct);
    }

    @PatchMapping("products/{productId:\\d+}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@RequestBody @Valid ProductUpdateRequestDTO request,
                                                            @PathVariable("productId") Long id,
                                                            BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        ProductResponseDTO updatedProduct = this.marketService.updateProduct(id, request);

        return ResponseEntity.ok().body(updatedProduct);
    }

    @DeleteMapping("products/{productId:\\d+}")
    public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable("productId") Long id) {
        int deleted = this.marketService.deleteProduct(id);

        if (deleted == 0) {
            throw new ProductNotFoundException("Product not found");
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @JsonView(Views.FullDetailWithId.class)
    @PostMapping("orders/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderCreateRequestDTO order,
                                                        BindingResult bindingResult,
                                                        UriComponentsBuilder uriComponentsBuilder) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        OrderResponseDTO response = this.marketService.createOrder(order);

        return ResponseEntity
                .created(uriComponentsBuilder.replacePath("/api/v1/orders/{orderId}")
                        .build(Map.of("orderId", response.id())))
                .body(response);
    }

    @JsonView(Views.OrderDetail.class)
    @GetMapping("orders/{orderId:\\d+}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable("orderId") Long id) {
        return ResponseEntity
                .ok()
                .body(this.marketService.findOrderById(id)
                        .orElseThrow(() -> new OrderNotFoundException("Order not found")));
    }
}
