package ru.skillbox.inventoryservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.event.EventHandler;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.event.PaymentEvent;
import ru.skillbox.inventoryservice.domain.InventoryStatus;
import ru.skillbox.inventoryservice.service.InventoryService;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.orderservice.domain.OrderStatus;
import ru.skillbox.orderservice.domain.ServiceName;
import ru.skillbox.orderservice.domain.StatusDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventHandler implements EventHandler<PaymentEvent, InventoryEvent> {

    private final InventoryService inventoryService;

    private final RestTemplate restTemplate;

    @Value("${service.order.url}")
    private String orderServiceUrl;

    @Override
    public InventoryEvent handleEvent(PaymentEvent paymentEvent) {
        log.info("Payment event received: {}", paymentEvent);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        StatusDto statusDto = new StatusDto();
        statusDto.setServiceName(ServiceName.INVENTORY_SERVICE);

        OrderDto orderDto = paymentEvent.getOrderDto();
        Long orderId = paymentEvent.getOrderId();

        InventoryEvent inventoryEvent = new InventoryEvent();
        inventoryEvent.setOrderId(orderId);
        inventoryEvent.setOrderDto(orderDto);
        if (inventoryService.inventOrder(orderDto)) {
            inventoryEvent.setInventoryStatus(InventoryStatus.COMPLETE.name());
            statusDto.setStatus(OrderStatus.INVENTED);
        } else {
            inventoryEvent.setInventoryStatus(InventoryStatus.INCOMPLETE.name());
            statusDto.setStatus(OrderStatus.INVENTMENT_FAILED);
        }
        restTemplate.exchange(orderServiceUrl + orderId, HttpMethod.PATCH, new HttpEntity<>(statusDto), Void.class);

        return inventoryEvent;
    }

}
