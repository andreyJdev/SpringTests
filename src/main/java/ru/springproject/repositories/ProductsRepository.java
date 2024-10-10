package ru.springproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.springproject.models.Product;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
}
