package ru.skillbox.deliveryservice.service;

import ru.skillbox.deliveryservice.domain.Delivery;
import ru.skillbox.orderservice.domain.OrderDto;

public interface DeliveryService {

    Delivery shipOrder(Long orderId, OrderDto orderDto);

}
