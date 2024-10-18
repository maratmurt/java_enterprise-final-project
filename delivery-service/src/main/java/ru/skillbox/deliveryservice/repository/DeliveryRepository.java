package ru.skillbox.deliveryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.deliveryservice.domain.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
