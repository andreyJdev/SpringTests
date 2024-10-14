package ru.springproject.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.SmartValidator;
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
    private final SmartValidator validator;

    @GetMapping("products")
    @JsonView(Views.FullDetailWithId.class)
    public ResponseEntity<String> getAllProducts() throws JsonProcessingException {
        String productsResponseJson = objectMapper.writeValueAsString(this.marketService.findAllProducts()
                .orElseThrow(() -> new ProductNotFoundException("No products found")));

        return ResponseEntity
                .ok()
                .body(productsResponseJson);
    }

    @GetMapping("products/{productId:\\d+}")
    @JsonView(Views.FullDetail.class)
    public ResponseEntity<String> getProductById(@PathVariable("productId") Long id) throws JsonProcessingException {
        String productResponseJson = objectMapper.writeValueAsString(this.marketService.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found")));

        return ResponseEntity
                .ok()
                .body(productResponseJson);
    }

    @PostMapping("products/create")
    public ResponseEntity<String> createProduct(@RequestBody String jsonRequest,
                                                UriComponentsBuilder uriComponentsBuilder) throws BindException, JsonProcessingException {
        ProductCreateRequestDTO request = objectMapper.readValue(jsonRequest, ProductCreateRequestDTO.class);

        BindingResult bindingResult = new BeanPropertyBindingResult(request, "productCreateRequestDTO");
        validator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        ProductResponseDTO createdProduct = this.marketService.createProduct(request);
        String productResponseJson = objectMapper.writeValueAsString(createdProduct);

        return ResponseEntity
                .created(uriComponentsBuilder.replacePath("/api/v1/products/{productId}")
                        .build(Map.of("productId", createdProduct.id())))
                .body(productResponseJson);
    }

    @PatchMapping("products/{productId:\\d+}")
    public ResponseEntity<String> updateProduct(@RequestBody String requestJson,
                                                @PathVariable("productId") Long id) throws BindException, JsonProcessingException {
        ProductUpdateRequestDTO request = objectMapper.readValue(requestJson, ProductUpdateRequestDTO.class);

        BindingResult bindingResult = new BeanPropertyBindingResult(request, "productUpdateRequestDTO");
        validator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        ProductResponseDTO updatedProduct = this.marketService.updateProduct(id, request);
        String updatedProductJson = objectMapper.writeValueAsString(updatedProduct);

        return ResponseEntity.ok().body(updatedProductJson);
    }

    @DeleteMapping("products/{productId:\\d+}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long id) {
        int deleted = this.marketService.deleteProduct(id);

        if (deleted == 0) {
            throw new ProductNotFoundException("Product not found");
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @JsonView(Views.FullDetailWithId.class)
    @PostMapping("orders/create")
    public ResponseEntity<String> createOrder(@RequestBody String requestJson,
                                                        UriComponentsBuilder uriComponentsBuilder) throws BindException, JsonProcessingException {
        OrderCreateRequestDTO request = objectMapper.readValue(requestJson, OrderCreateRequestDTO.class);

        BindingResult bindingResult = new BeanPropertyBindingResult(request, "orderCreateRequestDTO");
        validator.validate(request, bindingResult);

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        OrderResponseDTO response = this.marketService.createOrder(request);
        String orderResponseJson = objectMapper.writeValueAsString(response);

        return ResponseEntity
                .created(uriComponentsBuilder.replacePath("/api/v1/orders/{orderId}")
                        .build(Map.of("orderId", response.id())))
                .body(orderResponseJson);
    }

    @JsonView(Views.OrderDetail.class)
    @GetMapping("orders/{orderId:\\d+}")
    public ResponseEntity<String> getOrderById(@PathVariable("orderId") Long id) throws JsonProcessingException {
        String responseJson = objectMapper.writeValueAsString(this.marketService.findOrderById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found")));

        return ResponseEntity
                .ok()
                .body(responseJson);
    }
}
