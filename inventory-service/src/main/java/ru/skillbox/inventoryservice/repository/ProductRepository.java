package ru.skillbox.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.inventoryservice.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
