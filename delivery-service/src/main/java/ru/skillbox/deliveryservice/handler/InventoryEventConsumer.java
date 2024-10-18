package ru.skillbox.deliveryservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.deliveryservice.service.DeliveryService;
import ru.skillbox.event.EventConsumer;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.orderservice.domain.OrderStatus;
import ru.skillbox.orderservice.domain.ServiceName;
import ru.skillbox.orderservice.domain.StatusDto;

import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventConsumer implements EventConsumer<InventoryEvent> {

    private final DeliveryService deliveryService;

    private final RestTemplate restTemplate;

    @Value("${service.order.url}")
    private String orderServiceUrl;

    @Override
    public void consumeEvent(InventoryEvent event) {
        log.info("Received inventory event: {}", event);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        StatusDto statusDto = new StatusDto();
        statusDto.setServiceName(ServiceName.DELIVERY_SERVICE);

        if (deliveryService.shipOrder(event.getOrderId(), event.getOrderDto())) {
            statusDto.setStatus(OrderStatus.DELIVERED);
        } else {
            statusDto.setStatus(OrderStatus.DELIVERY_FAILED);
        }
        restTemplate.exchange(orderServiceUrl + event.getOrderId(), HttpMethod.PATCH, new HttpEntity<>(statusDto), Void.class);
    }
}
