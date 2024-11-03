package ru.skillbox.inventoryservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.event.DeliveryEvent;
import ru.skillbox.event.EventHandler;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.inventoryservice.service.InventoryService;
import ru.skillbox.orderservice.domain.DeliveryStatus;
import ru.skillbox.orderservice.domain.InventoryStatus;
import ru.skillbox.orderservice.domain.OrderDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryEventHandler implements EventHandler<DeliveryEvent, InventoryEvent> {

    private final InventoryService inventoryService;

    @Override
    public InventoryEvent handleEvent(DeliveryEvent deliveryEvent) {
        log.info("Received delivery event: {}", deliveryEvent);

        if (!deliveryEvent.getDeliveryStatus().equals(DeliveryStatus.RETURNED.name())) {
            return null;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        OrderDto orderDto = deliveryEvent.getOrderDto();

        inventoryService.receipt(orderDto.getInventory(), "Order returned");

        return new InventoryEvent(
                deliveryEvent.getOrderId(),
                deliveryEvent.getUsername(),
                InventoryStatus.RETURNED.name(),
                orderDto);
    }
}
