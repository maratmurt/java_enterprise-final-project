package ru.skillbox.deliveryservice.service;

import ru.skillbox.orderservice.domain.OrderDto;

public interface DeliveryService {

    boolean shipOrder(Long orderId, OrderDto orderDto);

}
