package ru.springproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.SmartValidator;
import ru.springproject.dto.ProductCreateRequestDTO;
import ru.springproject.dto.ProductResponseDTO;
import ru.springproject.dto.ProductUpdateRequestDTO;
import ru.springproject.services.MarketService;

import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class MarketControllerTest {

    @Mock
    MarketService marketService;

    @Mock
    private SmartValidator validator;

    @InjectMocks
    MarketController marketController;

    MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    void setUp() {
        marketController = new MarketController(marketService, objectMapper, validator);
        mockMvc = MockMvcBuilders.standaloneSetup(marketController).build();
    }

    @Test
    void getAllProducts_ReturnProducts() throws Exception {
        // given
        Set<ProductResponseDTO> products = Set.of(new ProductResponseDTO(1L, "Product 1", "Desc", 100, 10));
        when(marketService.findAllProducts()).thenReturn(Optional.of(products));
        String productsJson = objectMapper.writeValueAsString(products);

        // when
        mockMvc.perform(get("/api/v1/products"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(productsJson));

        verify(marketService).findAllProducts();
    }

    @Test
    void getProductById_ReturnProduct() throws Exception {
        // given
        ProductResponseDTO product = new ProductResponseDTO(1L, "Product 1", "Desc", 100, 10);
        when(marketService.findProductById(1L)).thenReturn(Optional.of(product));
        String productJson = objectMapper.writeValueAsString(product);

        // when
        mockMvc.perform(get("/api/v1/products/1"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(productJson));

        verify(marketService).findProductById(1L);
    }


    @Test
    void createProduct_ValidRequest_CreatesProduct() throws Exception {
        // given
        ProductCreateRequestDTO request = new ProductCreateRequestDTO("Product 1", "Desc", 100, 10);
        ProductResponseDTO response = new ProductResponseDTO(1L, "Product 1", "Desc", 100, 10);
        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);

        when(marketService.createProduct(request)).thenReturn(response);

        // when
        mockMvc.perform(post("/api/v1/products/create")
                        .contentType("application/json")
                        .content(requestJson))
                // then
                .andExpect(status().isCreated())
                .andExpect(content().json(responseJson));

        verify(marketService).createProduct(request);
    }


    @Test
    void updateProduct_ValidRequest_UpdatesProduct() throws Exception {
        // given
        ProductUpdateRequestDTO request = new ProductUpdateRequestDTO("Updated Product", "Updated Desc", 200, 20);
        ProductResponseDTO response = new ProductResponseDTO(1L, "Updated Product", "Updated Desc", 200, 20);
        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(response);

        when(marketService.updateProduct(1L, request)).thenReturn(response);

        // when
        mockMvc.perform(patch("/api/v1/products/1")
                        .contentType("application/json")
                        .content(requestJson))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));

        verify(marketService).updateProduct(1L, request);
    }

    @Test
    void deleteProduct_ValidRequest_DeletesProduct() throws Exception {
        // given
        when(marketService.deleteProduct(1L)).thenReturn(1);

        // when
        mockMvc.perform(delete("/api/v1/products/1"))
                // then
                .andExpect(status().isNoContent());

        verify(marketService).deleteProduct(1L);
    }
}