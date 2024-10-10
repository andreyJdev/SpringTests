package ru.springproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.springproject.models.Order;

public interface OrdersRepository extends JpaRepository<Order, Long> {
}
