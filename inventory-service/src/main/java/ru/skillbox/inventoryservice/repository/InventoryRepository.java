package ru.skillbox.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.inventoryservice.domain.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
