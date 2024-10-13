package ru.springproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.springproject.models.Customer;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT u FROM Customer u LEFT JOIN FETCH u.ordersList WHERE u.id = :id")
    Optional<Customer> findByIdWithOrder(Long id);

    int removeById(Long id);
}