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
import ru.springproject.models.Customer;
import ru.springproject.services.MarketService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
class MarketControllerTest {

    @Mock
    private MarketService marketService;

    @InjectMocks
    private MarketController marketController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(marketController).build();
    }

    @Test
    void getUsers_ReturnUsers() throws Exception {
        // given
        List<Customer> users = Arrays.asList(new Customer(1L, "John Doe", "john@example.com", new ArrayList<>()));
        when(marketService.findAllUsers()).thenReturn(users);

        // when
        mockMvc.perform(get("/api/v1/users"))

                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john@example.com"))
                // проверка, что поле скрыто через JsonView
                .andExpect(jsonPath("$[0].ordersList").doesNotExist());
    }

    @Test
    void getUserById_RequestIsValid_ReturnUser() throws Exception {
        // given
        Customer user = new Customer(1L, "John Doe", "john@example.com", new ArrayList<>());
        Set<OrderResponseDTO> orders = new HashSet<>();
        Map<UserResponseDTO, Set<OrderResponseDTO>> userWithOrders = Collections.singletonMap(new UserResponseDTO(user), orders);

        when(marketService.findUserByIdWithOrder(1L)).thenReturn(userWithOrders);

        // when
        String responseContent = mockMvc.perform(get("/api/v1/users/1"))

                // then
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        // в Json содержится пустой список
        assertTrue(responseContent.contains("[]"));
        // в Json содержится имя
        assertTrue(responseContent.contains("John Doe"));
    }

    @Test
    void createUser_RequestIsValid_ReturnsResponseEntity() throws Exception {
        // given
        Customer newUser = new Customer(1L, "John Doe", "john@example.com", null);
        when(marketService.saveUser(any(Customer.class))).thenReturn(newUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String newUserJson = objectMapper.writeValueAsString(newUser);

        // when
        mockMvc.perform(post("/api/v1/create")

                        // then
                        .contentType("application/json")
                        .content(newUserJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void updateUser_RequestIsValid_ReturnsResponseEntity() throws Exception {
        // given
        Customer updatedUser = new Customer(1L, "John Doe", "john@example.com", null);
        when(marketService.saveUser(any(Customer.class))).thenReturn(updatedUser);

        ObjectMapper objectMapper = new ObjectMapper();
        String updatedUserJson = objectMapper.writeValueAsString(updatedUser);

        // when
        mockMvc.perform(patch("/api/v1/update")

                        // then
                        .contentType("application/json")
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void deleteUser_RequestIsValid_ReturnsNoContent() throws Exception {
        // given
        when(marketService.deleteUser(1L)).thenReturn(1);

        // when
        mockMvc.perform(delete("/api/v1/users/1"))

                // then
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_RequestIsInvalid_ThrowsUserNotFoundException() {
        // given
        when(marketService.deleteUser(1L)).thenReturn(0);

        // when
        UserNotFoundException e = assertThrows(UserNotFoundException.class, () -> this.marketController.deleteUser(1L));

        // then
        assertEquals("errors.user.not_found", e.getMessage());
    }
}