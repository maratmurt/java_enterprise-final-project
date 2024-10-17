package ru.skillbox.inventoryservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.event.EventHandler;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.event.PaymentEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventHandler implements EventHandler<PaymentEvent, InventoryEvent> {
    @Override
    public InventoryEvent handleEvent(PaymentEvent paymentEvent) {
        log.info("Payment event received: {}", paymentEvent);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



        return null;
    }
}
