package ru.skillbox.deliveryservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.deliveryservice.domain.Delivery;
import ru.skillbox.orderservice.domain.DeliveryStatus;
import ru.skillbox.deliveryservice.repository.DeliveryRepository;
import ru.skillbox.orderservice.domain.OrderDto;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    public Delivery shipOrder(Long orderId, OrderDto orderDto) {
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setDepartureAddress(orderDto.getDepartureAddress());
        delivery.setDestinationAddress(orderDto.getDestinationAddress());

        Random random = new Random(orderId);
        if (random.nextInt(100) >= 15) {
            delivery.setStatus(DeliveryStatus.DELIVERED);
        } else if (random.nextBoolean()){
            delivery.setStatus(DeliveryStatus.LOST);
        } else {
            delivery.setStatus(DeliveryStatus.RETURNED);
        }

        return deliveryRepository.save(delivery);
    }

}
