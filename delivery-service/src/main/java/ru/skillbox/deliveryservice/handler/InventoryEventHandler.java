package ru.skillbox.deliveryservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.orderservice.domain.DeliveryStatus;
import ru.skillbox.deliveryservice.service.DeliveryService;
import ru.skillbox.event.DeliveryEvent;
import ru.skillbox.event.EventHandler;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.orderservice.domain.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventHandler implements EventHandler<InventoryEvent, DeliveryEvent> {

    private final DeliveryService deliveryService;

    private final RestTemplate restTemplate;

    @Value("${service.order.url}")
    private String orderServiceUrl;

    @Override
    public DeliveryEvent handleEvent(InventoryEvent inventoryEvent) {
        log.info("Received inventory event: {}", inventoryEvent);

        if (!inventoryEvent.getInventoryStatus().equals(InventoryStatus.COMPLETE.name())) {
            return null;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Long orderId = inventoryEvent.getOrderId();
        OrderDto orderDto = inventoryEvent.getOrderDto();

        StatusDto orderStatus = new StatusDto();
        orderStatus.setServiceName(ServiceName.DELIVERY_SERVICE);

        DeliveryStatus deliveryStatus = deliveryService.shipOrder(orderId, orderDto).getStatus();

        if (deliveryStatus.equals(DeliveryStatus.DELIVERED)) {
            orderStatus.setStatus(OrderStatus.DELIVERED);
        } else {
            orderStatus.setStatus(OrderStatus.DELIVERY_FAILED);
        }
        restTemplate.exchange(orderServiceUrl + orderId, HttpMethod.PATCH, new HttpEntity<>(orderStatus), Void.class);

        return new DeliveryEvent(
                orderId,
                inventoryEvent.getUsername(),
                deliveryStatus.name(),
                orderDto);
    }
}
