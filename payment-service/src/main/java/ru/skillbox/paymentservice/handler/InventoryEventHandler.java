package ru.skillbox.paymentservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.event.EventHandler;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.event.PaymentEvent;
import ru.skillbox.orderservice.domain.InventoryStatus;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.orderservice.domain.PaymentStatus;
import ru.skillbox.paymentservice.service.PaymentService;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventHandler implements EventHandler<InventoryEvent, PaymentEvent> {

    private final PaymentService paymentService;

    @Override
    public PaymentEvent handleEvent(InventoryEvent inventoryEvent) {
        log.info("Received inventory event: {}", inventoryEvent);

        if (!inventoryEvent.getInventoryStatus().equals(InventoryStatus.INCOMPLETE.name())) {
            return null;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Long orderId = inventoryEvent.getOrderId();
        OrderDto orderDto = inventoryEvent.getOrderDto();
        String username = inventoryEvent.getUsername();

        paymentService.refundOrder(username, orderId, orderDto.getCost());

        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setOrderId(orderId);
        paymentEvent.setOrderDto(orderDto);
        paymentEvent.setPaymentStatus(PaymentStatus.REFUND.name());

        return paymentEvent;
    }
}
