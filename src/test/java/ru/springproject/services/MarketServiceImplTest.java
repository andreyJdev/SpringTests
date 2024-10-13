package ru.springproject.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.springproject.models.Customer;
import ru.springproject.repositories.UsersRepository;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class MarketServiceImplTest {

    @Mock
    UsersRepository usersRepository;

    @InjectMocks
    MarketServiceImpl marketService;

    @Test
    void findAllUsers_ReturnUsers() {
        // given
        List<Customer> users = Arrays.asList(new Customer(1L, "John Doe", "john@example.com", new ArrayList<>()));
        when(usersRepository.findAll()).thenReturn(users);

        // when
        List<Customer> result = marketService.findAllUsers();

        // then
        assertEquals(users, result);
        verify(usersRepository).findAll();
        verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void findUserByIdWithOrder_UserExists_ReturnUserWithOrders() {
        // given
        Customer user = new Customer(1L, "John Doe", "john@example.com", new ArrayList<>());
        Set<OrderResponseDTO> orders = new HashSet<>();
        Map<UserResponseDTO, Set<OrderResponseDTO>> expectedResponse = Collections.singletonMap(new UserResponseDTO(user), orders);

        when(usersRepository.findByIdWithOrder(1L)).thenReturn(Optional.of(user));

        // when
        Map<UserResponseDTO, Set<OrderResponseDTO>> result = marketService.findUserByIdWithOrder(1L);

        // then
        assertEquals(expectedResponse, result);
        verify(usersRepository).findByIdWithOrder(1L);
        verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void findUserByIdWithOrder_UserDoesNotExist_ThrowsUserNotFoundException() {
        // given
        when(usersRepository.findByIdWithOrder(1L)).thenReturn(Optional.empty());

        // when
        UserNotFoundException e = assertThrows(UserNotFoundException.class, () -> marketService.findUserByIdWithOrder(1L));

        // then
        assertEquals("errors.user.not_found", e.getMessage());
        verify(usersRepository).findByIdWithOrder(1L);
        verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void saveUser_ValidUser_SavesUser() {
        // given
        Customer user = new Customer(null, "John Doe", "john@example.com", new ArrayList<>());
        when(usersRepository.save(user)).thenReturn(user);

        // when
        Customer result = marketService.saveUser(user);

        // then
        assertEquals(user, result);
        verify(usersRepository).save(user);
        verifyNoMoreInteractions(usersRepository);
    }

    @Test
    void deleteUser_UserExists_RemovesUser() {
        // given
        when(usersRepository.removeById(1L)).thenReturn(1);

        // when
        int result = marketService.deleteUser(1L);

        // then
        assertEquals(1, result);
        verify(usersRepository).removeById(1L);
        verifyNoMoreInteractions(usersRepository);
    }
}