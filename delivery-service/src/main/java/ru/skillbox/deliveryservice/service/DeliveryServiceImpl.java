package ru.skillbox.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.deliveryservice.domain.Delivery;
import ru.skillbox.deliveryservice.domain.DeliveryStatus;
import ru.skillbox.deliveryservice.repository.DeliveryRepository;
import ru.skillbox.orderservice.domain.OrderDto;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    public boolean shipOrder(Long orderId, OrderDto orderDto) {
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setDepartureAddress(orderDto.getDepartureAddress());
        delivery.setDestinationAddress(orderDto.getDestinationAddress());

        Random random = new Random(orderId);
        boolean isDelivered = random.nextInt(100) >= 50;
        if (isDelivered) {
            delivery.setStatus(DeliveryStatus.DELIVERED);
        } else {
            delivery.setStatus(DeliveryStatus.LOST);
        }
        deliveryRepository.save(delivery);

        return isDelivered;
    }

}
